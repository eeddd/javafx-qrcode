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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
        if (txtURL.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Empty URL");
            alert.setContentText("Please input URL");
            alert.showAndWait();
            return;
        }
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

    @FXML
    protected void handleSaveAs(ActionEvent event)
    {
        if (imageView.getImage() == null) {
            if (txtURL.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("No QR code generated");
                alert.setContentText("Make sure you have generated a QR code before saving");
                alert.showAndWait();
                return;
            }
            return;
        }

        Button btn = (Button) event.getSource();

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image","*.png","*.jpg"));

        File file = chooser.showSaveDialog(btn.getScene().getWindow());
        if (file == null) {
            return;
        }

        Alert alert;
        try {
            Image img = imageView.getImage();
            BufferedImage bi = SwingFXUtils.fromFXImage(img, null);
            if (file.getName().toLowerCase().endsWith("png")) {
                ImageIO.write(bi, "png", file);
            } else {
                ImageIO.write(bi, "jpg", file);
            }
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("File saved!");
            alert.showAndWait();
        } catch (Exception ex) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error saving image");
            alert.showAndWait();
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
