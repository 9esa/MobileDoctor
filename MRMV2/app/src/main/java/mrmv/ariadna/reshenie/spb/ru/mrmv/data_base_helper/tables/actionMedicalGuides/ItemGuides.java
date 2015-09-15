package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuides;

/**
 * Created by kirichenko on 02.05.2015.
 * Класс для представления справочника в Spinner
 * с переопределенным toString
 */
public class ItemGuides {
        private String sIdGuides;
        private String sCode;
        private String sText;
        private String sShortText;
        private String sTag;
        private String sIsDefault;
        private String sMultiValue = "0";

    public String getsIsDefault() {
        return sIsDefault;
    }

    public String getsMultiValue() {
        return sMultiValue;
    }

    public void setsMultiValue(String sMultiValue) {
        this.sMultiValue = sMultiValue;
    }

    public void setsIsDefault(String sIsDefault) {
        this.sIsDefault = sIsDefault;
    }

    public String getsIdGuides() {
            return sIdGuides;
        }

        public void setsIdGuides(String sIdGuides) {
            this.sIdGuides = sIdGuides;
        }

        public String getsCode() {
            return sCode;
        }

        public void setsCode(String sCode) {
            this.sCode = sCode;
        }

        public String getsText() {
            return sText;
        }

        public void setsText(String sText) {
            this.sText = sText;
        }

        public String getsShortText() {
            return sShortText;
        }

        public void setsShortText(String sShortText) {
            this.sShortText = sShortText;
        }

        public String getsTag() {
            return sTag;
        }

        public void setsTag(String sTag) {
            this.sTag = sTag;
        }

        @Override
        public String toString() {
            return sText;
        }
    }
