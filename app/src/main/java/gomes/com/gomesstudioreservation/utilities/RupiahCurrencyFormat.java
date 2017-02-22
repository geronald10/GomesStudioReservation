package gomes.com.gomesstudioreservation.utilities;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class RupiahCurrencyFormat {

    public String toRupiahFormat(String harga) {
        String rupiah;
        double formatHarga = Double.parseDouble(harga);

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        rupiah = kursIndonesia.format(formatHarga);
        return rupiah;
    }
}