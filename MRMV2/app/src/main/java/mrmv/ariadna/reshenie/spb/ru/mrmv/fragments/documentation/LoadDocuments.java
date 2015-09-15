package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.documentation;

import android.content.Context;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.ILoginEnableAccess;

/**
 * Created by kirichenko on 07.09.2015.
 */
public class LoadDocuments {

    public static final String PATH_TO_SAVE = "/storage/sdcard0/Ariadna";

    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private String sAddress;

    private Context mContext;

    private ILoginEnableAccess iLoginEnableAccess;

    public LoadDocuments(LoginAccount oLoginAccount, DataBaseHelper oDataBaseHelper, String addressForRequest, Context mContext) {
        this.mContext = mContext;
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = oDataBaseHelper;
        this.sAddress = addressForRequest;
    }

    public int startLoadDocuments(ILoginEnableAccess iLoginEnableAccess) {
        this.iLoginEnableAccess = iLoginEnableAccess;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startDownLoad();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        return 1;
    }

    private void startDownLoad() throws IOException {

        String sValue = "";

        ///tablet_doc/
        String server = "p112.spb.ru";
        int port = 21;
        //String user = "anonymous";
        //String pass = "anonymous@domain.com";

        String user = "guest";
        String pass = "";


        File file = new File(PATH_TO_SAVE);

        if (!file.exists()) {
            file.mkdir();
        }

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server, port);

            ftpClient.enterLocalPassiveMode();

            ftpClient.connect(server, port);

            ftpClient.login(user, pass);

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            ftpClient.enterLocalPassiveMode();

            ftpClient.setControlEncoding("UTF-8");

            FTPFile[]ftpFiles =  ftpClient.listFiles();

            for(FTPFile ftpFile : ftpFiles){

                sValue = ftpFile.getName();

                file = new File(PATH_TO_SAVE, ftpFile.getName());

                FileOutputStream fos = new FileOutputStream(file);

                ftpClient.retrieveFile(ftpFile.getName(), fos);

                fos.close();
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();

        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }

                iLoginEnableAccess.enableAccessLoadFinished(oLoginAccount);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean unpackZip(String path, String zipname)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
