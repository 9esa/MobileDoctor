package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.documentation;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;

/**
 * Created by kirichenko on 02.09.2015.
 */
public class DocumentationFragment extends Fragment {

    public static String PATH_TO_SAVE = "/storage/sdcard0/Ariadna";

    private ImageButton ibtnRefresh;

    private ListView oListView;

    private List <String> listOfNameFiles;
    private ArrayList <File> listOfFiles;
    private ArrayList <File> listOfPath;

    int iCurrentLvl = 0;

    private File oCurrentFile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View oView = inflater.inflate(R.layout.documentation_fragment, container, false);

        oListView = (ListView)oView.findViewById(R.id.lvElementsOfEnableDocuments);
        ibtnRefresh = (ImageButton)oView.findViewById(R.id.btnRefreshDocuments);

        listOfNameFiles = new ArrayList<>();
        listOfFiles = new ArrayList<>();
        listOfPath =  new ArrayList<>();

        viewFilesInDir(new File(PATH_TO_SAVE));

        ibtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oCurrentFile != null){
                    viewFilesInDir(oCurrentFile);
                    Toast.makeText(getActivity(), R.string.update_files_correct_loaded, Toast.LENGTH_SHORT).show();
                }
            }
        });


        return oView;
    }

    private void viewFilesInDir(final File oOldFile){

        listOfNameFiles.clear();
        listOfFiles.clear();

        if( oOldFile!= null && iCurrentLvl != 0){
            listOfNameFiles.add("...");
            listOfFiles.add(listOfPath.get(iCurrentLvl - 1));
        }

        oCurrentFile = oOldFile;

        getListFiles(oOldFile);

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listOfNameFiles);

        oListView.setAdapter(itemsAdapter);

        oListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int iSelectIndex, long l) {

                if (listOfFiles.get(iSelectIndex).isDirectory()) {

                    if(iCurrentLvl != 0 && iSelectIndex == 0){

                        iCurrentLvl--;
                        viewFilesInDir(listOfPath.get(iCurrentLvl));

                        if(iCurrentLvl == 0){
                            listOfPath.clear();
                        }

                    }else{

                        listOfPath.add(iCurrentLvl,oOldFile);
                        iCurrentLvl++;

                        viewFilesInDir(listOfFiles.get(iSelectIndex));
                    }

                    return;
                }

                Uri path = Uri.fromFile(listOfFiles.get(iSelectIndex));
                Intent oNewIntentOpen = new Intent(Intent.ACTION_VIEW);

                oNewIntentOpen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                oNewIntentOpen.setDataAndType(path, "application/msword");

                try {
                    startActivity(oNewIntentOpen);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
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

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            listOfNameFiles.add(file.getName());
            listOfFiles.add(file);


//            if (file.isDirectory()) {
//                inFiles.addAll(getListFiles(file));
//            } else {
//                if(file.getName().endsWith(".csv")){
//                    inFiles.add(file);
//                }
//            }
        }
        return inFiles;
    }

    private void startDownLoad() throws IOException {

        ///tablet_doc/
        String server = "p112.spb.ru";
        int port = 21;
        //String user = "anonymous";
        //String pass = "anonymous@domain.com";

        String user = "guest";
        String pass = "";

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

                File file = new File(PATH_TO_SAVE, ftpFile.getName());

                FileOutputStream fos = new FileOutputStream(file);

                ftpClient.retrieveFile(ftpFile.getName(),fos);

                fos.close();

                Uri path = Uri.fromFile(file);

                Intent oNewIntentOpen = new Intent(Intent.ACTION_VIEW);

                oNewIntentOpen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                oNewIntentOpen.setDataAndType(path, "application/msword");

                    try {
                        startActivity(oNewIntentOpen);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }

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
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
