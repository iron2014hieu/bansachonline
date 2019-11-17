package iron2014.bansachonline.nighmode_vanchuyen.vanchuyen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

import iron2014.bansachonline.R;

public class CountryAdapter extends ArrayAdapter<CountryItem> {

    public CountryAdapter(Context context, ArrayList<CountryItem> countryList) {
        super(context, 0, countryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_spinner, parent, false
            );
        }

        ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);
        TextView textViewName = convertView.findViewById(R.id.text_view_name);
        TextView textViewPrice = convertView.findViewById(R.id.text_view_price);
        CountryItem currentItem = getItem(position);

        if (currentItem != null) {
            imageViewFlag.setImageResource(currentItem.getFlagImage());
            textViewName.setText(currentItem.getCountryName());
            textViewPrice.setText(currentItem.getPrice());
        }

        return convertView;
    }
}
