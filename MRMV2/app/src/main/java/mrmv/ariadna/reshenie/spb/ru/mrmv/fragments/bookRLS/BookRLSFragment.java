    package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.bookRLS;

    import android.content.ComponentName;
    import android.content.Intent;
    import android.content.ServiceConnection;
    import android.database.Cursor;
    import android.os.Bundle;
    import android.os.IBinder;
    import android.support.annotation.Nullable;
    import android.support.v4.app.Fragment;
    import android.support.v4.app.FragmentActivity;
    import android.support.v4.app.LoaderManager;
    import android.support.v4.content.CursorLoader;
    import android.support.v4.content.Loader;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.FrameLayout;
    import android.widget.ListView;
    import android.widget.Toast;

    import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
    import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
    import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
    import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
    import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.rlsAction.RLSItem;
    import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceLoading;

    /**
     * Created by kirichenko on 12.09.2015.
     */
    public class BookRLSFragment extends Fragment implements ICommonLoadComplete, LoaderManager.LoaderCallbacks<Cursor> {

        private EditText etProductName, etNameOfActiveComponent, etNameOfGroupComponent;
        private ListView lvFindProduct;
        private Button btnFindProduct;

        private FrameLayout flMessageEmptyValue, flProgressBar;

        static private LoginAccount oLoginAccount;

        private ServiceLoading oServiceLoading;
        private ServiceConnection sConnectionChecking;
        private boolean bConnectToServiceDownload = false;
        private Intent intentCheckingData;

        private DataBaseHelper oDataBaseHelper;

        private BookRLSAdapter oBookRLSAdapter;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View oView = inflater.inflate(R.layout.book_rls_fragment, null, false);

            getAllViewElements(oView);

            startAllServices();

            return oView;

        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            oDataBaseHelper = DataBaseHelper.getInstance(getActivity());

            oBookRLSAdapter = new BookRLSAdapter(getActivity(), null);
        }

        private void getAllViewElements(View oView) {

            etProductName = (EditText) oView.findViewById(R.id.etProductName);
            etNameOfActiveComponent = (EditText) oView.findViewById(R.id.etNameOfActiveComponent);
            etNameOfGroupComponent = (EditText) oView.findViewById(R.id.etNameOfGroupComponent);

            flMessageEmptyValue = (FrameLayout) oView.findViewById(R.id.flMessageEmptyValue);
            flProgressBar = (FrameLayout) oView.findViewById(R.id.flProgressBar);

            lvFindProduct = (ListView) oView.findViewById(R.id.lvListOfProduct);

            lvFindProduct.setAdapter(oBookRLSAdapter);

            lvFindProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int iItemSelected, long l) {

                    if (view != null) {

                        Cursor oCurrentSelectedElements = ((Cursor) adapterView.getAdapter().getItem(iItemSelected));

                        String sCode = null, sTradename = null, sLatname = null,
                                sNdv = null, sLive = null, sForm = null, sDosage = null,
                                sFilling = null, sPacking = null, sManufacturer = null,
                                sMcountry = null, sRegnum = null, sRegdate = null,
                                sRegistrator = null, sRcountry = null, sAge = null, sGrpname = null, sAtc = null;

                        if (oCurrentSelectedElements != null) {

                            sCode = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.CODE));
                            sTradename = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.TRADENAME));
                            sLatname = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.LATNAME));
                            sNdv = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.NDV));
                            sLive = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.LIFE));
                            sForm = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.FORM));
                            sDosage = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.DOSAGE));
                            sFilling = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.FILLING));
                            sPacking = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.PACKING));
                            sManufacturer = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.MANUFACTURER));
                            sMcountry = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.MCOUNTRY));
                            sRegnum = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.REGNUM));
                            sRegdate = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.REGDATE));
                            sRegistrator = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.REGISTRATOR));
                            sRcountry = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.RCOUNTRY));
                            sAge = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.AGE));
                            sGrpname = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.GRPNAME));
                            sAtc = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(RLSItem.ATC));

                            MainInformationRLS oMainInformationRLS = new MainInformationRLS();
                            oMainInformationRLS.setsCode(sCode);
                            oMainInformationRLS.setsTradename(sTradename);
                            oMainInformationRLS.setsLatname(sLatname);
                            oMainInformationRLS.setsNdv(sNdv);
                            oMainInformationRLS.setsLive(sLive);
                            oMainInformationRLS.setsForm(sForm);
                            oMainInformationRLS.setsDosage(sDosage);
                            oMainInformationRLS.setsFilling(sFilling);
                            oMainInformationRLS.setsPacking(sPacking);
                            oMainInformationRLS.setsManufacturer(sManufacturer);
                            oMainInformationRLS.setsMcountry(sMcountry);
                            oMainInformationRLS.setsRegnum(sRegnum);
                            oMainInformationRLS.setsRegdate(sRegdate);
                            oMainInformationRLS.setsRegistrator(sRegistrator);
                            oMainInformationRLS.setsRcountry(sRcountry);
                            oMainInformationRLS.setsAge(sAge);
                            oMainInformationRLS.setsGrpname(sGrpname);
                            oMainInformationRLS.setsAtc(sAtc);

                            RLSItemFragment dialogShowAboutProduct = new RLSItemFragment();
                            dialogShowAboutProduct.setObjectWithInformation(oMainInformationRLS);
                            dialogShowAboutProduct.show(getFragmentManager(), "dialogShowAboutProduct");
                        }
                    }


                }
            });

            btnFindProduct = (Button) oView.findViewById(R.id.btnFoundProduct);

            btnFindProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    lvFindProduct.setVisibility(View.GONE);
                    flProgressBar.setVisibility(View.VISIBLE);
                    flProgressBar.bringToFront();

                    makeObjectForRequest();
                }
            });
        }

        private void makeObjectForRequest() {

            ItemRLS oItemRLS = new ItemRLS();

            if (etProductName != null) {
                String sValue = etProductName.getText().toString();

                if (sValue.equals("")) {
                    oItemRLS.setsProductName(null);
                } else {
                    oItemRLS.setsProductName(sValue);
                }
            }

            if (etNameOfActiveComponent != null) {
                String sValue = etNameOfActiveComponent.getText().toString();

                if (sValue.equals("")) {
                    oItemRLS.setsNameOfActiveComponent(null);
                } else {
                    oItemRLS.setsNameOfActiveComponent(sValue);
                }
            }

            if (etNameOfGroupComponent != null) {
                String sValue = etNameOfGroupComponent.getText().toString();

                if (sValue.equals("")) {
                    oItemRLS.setsNameOfGroupComponent(null);
                } else {
                    oItemRLS.setsNameOfGroupComponent(sValue);
                }
            }

            requestObjectFromServer(oItemRLS);

        }

        public static LoginAccount getoLoginAccount() {
            return oLoginAccount;
        }

        public static void setoLoginAccount(LoginAccount oLoginAccount) {
            BookRLSFragment.oLoginAccount = oLoginAccount;
        }

        private void requestObjectFromServer(ItemRLS oItemRLS) {

            if (bConnectToServiceDownload) {
                oServiceLoading.getListProducts(getoLoginAccount(), oItemRLS, this);
            } else {
                Toast.makeText(getActivity(), R.string.service_found_not_ready, Toast.LENGTH_LONG).show();
            }

        }


        private void startAllServices() {

            intentCheckingData = new Intent(getActivity(), ServiceLoading.class);

            sConnectionChecking = new ServiceConnection() {

                public void onServiceConnected(ComponentName name, IBinder binder) {

                    oServiceLoading = ((ServiceLoading.ServiceBinder) binder).getService();
                    bConnectToServiceDownload = true;

                }

                public void onServiceDisconnected(ComponentName name) {

                    bConnectToServiceDownload = false;

                }
            };

            getActivity().bindService(intentCheckingData, sConnectionChecking, getActivity().BIND_AUTO_CREATE);

        }

        private void initLoadListFromDataBase() {
            getLoaderManager().restartLoader(0, new Bundle(), this);
        }

        @Override
        public void loadCompleted() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initLoadListFromDataBase();
                    }
                });
            }
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoaderForRLS(getActivity(), oDataBaseHelper);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor oCursor) {
            oBookRLSAdapter.swapCursor(oCursor);

            flProgressBar.setVisibility(View.GONE);

            if (oCursor != null) {
                if (oCursor.getCount() == 0) {
                    lvFindProduct.setVisibility(View.GONE);
                    flMessageEmptyValue.setVisibility(View.VISIBLE);
                } else {
                    lvFindProduct.setVisibility(View.VISIBLE);
                    flMessageEmptyValue.setVisibility(View.GONE);
                }
            }

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            oBookRLSAdapter.swapCursor(null);
        }

        static class CursorLoaderForRLS extends CursorLoader {

            private DataBaseHelper oDataBaseHelper;

            public CursorLoaderForRLS(FragmentActivity context, DataBaseHelper oDataBaseHelper) {
                super(context);
                this.oDataBaseHelper = oDataBaseHelper;
            }

            @Override
            public Cursor loadInBackground() {
                return RLSItem.getSearchValue(oDataBaseHelper);
            }
        }
    }
