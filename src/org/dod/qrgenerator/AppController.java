package org.dod.qrgenerator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    @FXML
    private TextField txtURL;
    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    protected void handleConvert(ActionEvent event)
    {
        if (txtURL.getText().isEmpty()) return;
        try {
            final BufferedImage bi = generateData(txtURL.getText());
            WritableImage img = SwingFXUtils.toFXImage(bi, null);
            imageView.setFitWidth(img.getWidth());
            imageView.setFitHeight(img.getHeight());
            imageView.setImage(img);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    private BufferedImage generateData(final String url) throws IOException, WriterException {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 600, 600);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            BufferedImage bi = MatrixToImageWriter.toBufferedImage(bitMatrix);
            return bi;
        } catch (Exception ex)
        {
            throw ex;
        }
    }
}
