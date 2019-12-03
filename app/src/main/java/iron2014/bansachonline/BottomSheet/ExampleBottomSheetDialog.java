package iron2014.bansachonline.BottomSheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import iron2014.bansachonline.R;
import iron2014.bansachonline.Session.SessionManager;

public class ExampleBottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    SessionManager sessionManager;
    String linkImage, tensach,giaban,kho;
    ImageView img_book_bt, img_close;
    TextView txtBTGiaban,txtKho;
    int soluong =1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        sessionManager = new SessionManager(getContext());

        final Button button1 = v.findViewById(R.id.button1);
        ElegantNumberButton elegantNumberButton = v.findViewById(R.id.btn_numberQuality_bs);
        img_book_bt = v.findViewById(R.id.img_book_bt);
        img_close= v.findViewById(R.id.img_close);
        txtBTGiaban= v.findViewById(R.id.txtBTGiaban);
        txtKho = v.findViewById(R.id.txtKho);

        HashMap<String,String> book = sessionManager.getBottonsheetBook();
        linkImage = book.get(sessionManager.ANHBIA);
        tensach = book.get(sessionManager.TENSACH);
        giaban = book.get(sessionManager.GIA);
        kho = book.get(sessionManager.SOLUONG);

        txtBTGiaban.setText(giaban+"đ");
        txtKho.setText(getString(R.string.kho) +kho);

        Picasso.with(getContext()).load(linkImage).into(img_book_bt);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, final int oldValue, final int newValue) {

                soluong = newValue;
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soluong > Integer.valueOf(kho)){
                    Toast.makeText(getContext(), getString(R.string.sachs) +tensach+getString(R.string.chicon) +kho+getString(R.string.cuon) , Toast.LENGTH_SHORT).show();

                }else {
                    mListener.onButtonClicked(String.valueOf(soluong));
                    dismiss();
                }
            }
        });

        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
