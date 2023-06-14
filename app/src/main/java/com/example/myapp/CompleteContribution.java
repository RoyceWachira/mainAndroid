package com.example.myapp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class CompleteContribution extends Fragment {

    private TextView txtContDate,txtContAmount,txtNextCont;
    private Button btnDone,btnPrint;
    String chamaId,chamaName,chamaFlow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_complete_contribution, container, false);

        Bundle arguments = getArguments();
        if(arguments != null) {
            String id = arguments.getString("contribution_id");
            chamaId = String.valueOf(arguments.getInt("chama_id"));
            chamaFlow= arguments.getString("chamaFlow");
            chamaName= arguments.getString("chamaName");
            String amount = arguments.getString("contribution_amount");
            String date = arguments.getString("contribution_date");
            String next = arguments.getString("contribution_date");


            txtContAmount = view.findViewById(R.id.txtContAmount);
            txtContAmount.setText(amount);

            txtContDate = view.findViewById(R.id.txtContDate);
            txtContDate.setText(date);

            txtNextCont=view.findViewById(R.id.txtContNext);
            txtNextCont.setText(next);
        }

        btnDone=view.findViewById(R.id.btnContDone);
        btnPrint= view.findViewById(R.id.btnContPrint);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChamaHomeFragment chamaHomeFragment= new ChamaHomeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("chamaId",chamaId);
                bundle.putString("chamaName",chamaName);
                bundle.putString("chamaFlow",chamaFlow);
                chamaHomeFragment.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.chamaFrameLayout, chamaHomeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void createPdf () throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "ContributionsPDF.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        document.setMargins(0, 0, 0, 0);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.bb);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] bitmapData = stream.toByteArray();

        Bundle arguments = getArguments();
        if (arguments != null) {
            chamaName = arguments.getString("chamaName");
            String amount = arguments.getString("contribution_amount");
            String nextdate = arguments.getString("next_contribution_date");
            String date = arguments.getString("contribution_date");


            ImageData imageData = ImageDataFactory.create(bitmapData);
            Image image = new Image(imageData);

            float columnWidth[] = {120, 220, 120, 100};
            Table table1 = new Table(columnWidth);

            // Add table rows with the argument values

            table1.addCell(new Cell().add(new Paragraph("Chama Name").setFontSize(25).setFontColor(ColorConstants.WHITE)).setBorder(Border.NO_BORDER));
            table1.addCell(new Cell().add(new Paragraph(chamaName).setFontSize(25).setFontColor(ColorConstants.WHITE)).setBorder(Border.NO_BORDER));

            DeviceRgb setColour = new DeviceRgb(8, 106, 119);
            float columnWidth2[] = {220, 120, 100, 120};
            Table table2 = new Table(columnWidth2);
            table2.setMargin(20);

            table2.addCell(new Cell().add(new Paragraph("Contribution Amount").setFontSize(20).setFontColor(ColorConstants.WHITE)));
            table2.addCell(new Cell().add(new Paragraph("Contribution Date").setFontSize(20).setFontColor(ColorConstants.WHITE)));
            table2.addCell(new Cell().add(new Paragraph("Next Contribution Date").setFontSize(20).setFontColor(ColorConstants.WHITE)));

            table2.addCell(new Cell().add(new Paragraph(amount).setFontSize(20).setFontColor(ColorConstants.WHITE)));
            table2.addCell(new Cell().add(new Paragraph(date).setFontSize(20).setFontColor(ColorConstants.WHITE)));
            table2.addCell(new Cell().add(new Paragraph(nextdate).setFontSize(20).setFontColor(ColorConstants.WHITE)));


            document.add(image.setFixedPosition(0, 0));
            document.add(table1);
            document.add(table2);
            document.close();
            showToast("Pdf Generated Successfully",false);
        }
    }

    private void showToast(String message, boolean isError) {
        LayoutInflater inflater = getLayoutInflater();
        View layout;

        if (isError) {
            layout = inflater.inflate(R.layout.custom_toast_error, getView().findViewById(R.id.toast_message));
        } else {
            layout = inflater.inflate(R.layout.custom_toast_success, getView().findViewById(R.id.toast_message));
        }

        TextView toastMessage = layout.findViewById(R.id.toast_message);
        toastMessage.setText(message);

        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}