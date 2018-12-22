package securityApp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;





public class mainWindowController
{

    @FXML
    private Button encrypt_btn;

    @FXML
    private Pane mainWindow;

    @FXML
    private Button decrypt_btn;

    @FXML
    private Label inform_label;

    @FXML
    private TextArea plain_text_area;

    @FXML
    private TextArea decrypt_area;

    @FXML
    private TextField input_textfield;

    @FXML
    private Button search_btn;

    byte[] encrypt;

    byte[] decrypt;

    byte [] msg;


    /**
     * This method is used to select the file from the computer.
     * Only text files are available for selection.
     *
     * @param event the event that was invoked when the button was pressed
     * @throws FileNotFoundException thrown is the file is not found
     */
    @FXML
    void selectFileHandler(ActionEvent event) throws IOException
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("open files");
        //filters the selections to be only .txt files
//        FileChooser.ExtensionFilter extFilter =
//                new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
//        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(new Stage());

        if(file == null)
        {
            input_textfield.setText("");
        }
        else
        {
            input_textfield.setText(file.getAbsolutePath());
            FileInputStream input = new FileInputStream(file);
            int i;
//            String msg="";
            msg = new byte[input.available()];
            int counter=0;
            try {
                while((i = input.read())!= -1)
                    msg[counter++] += (byte)i;
            }catch(IOException e){}
            plain_text_area.appendText(new String(msg, "UTF-8"));
            input.close();
        }
    }//end of selectFileHandler


    /**
     * This method is invoked when the "encrypt" button is pressed.
     * Encrypts the text file that was provided
     *
     * @param event the event that was invoked when the button was pressed
     */
    @FXML
    void encryptHandler(ActionEvent event) throws FileNotFoundException
    {
        FileOutputStream output = new FileOutputStream(input_textfield.getText());
        if(input_textfield.getText() == null || input_textfield.getText().trim().isEmpty())
        {
            errorMessage("No input file","Please select a file to be Encrypted!!!");
        }
        else
        {
            try
            {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                SecretKeySpec skeySpec = new SecretKeySpec("moutsopoulos1234".getBytes("UTF-8"), "AES");
                String ivBytes = "1234567891123456";
                IvParameterSpec iv = new IvParameterSpec(ivBytes.getBytes("UTF-8"));
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
                encrypt = cipher.doFinal(msg);
                output.write(encrypt);
                plain_text_area.clear();
                decrypt_area.appendText(new String(encrypt, "UTF-8"));
                decrypt_btn.setDisable(false);
                encrypt_btn.setDisable(true);

            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            catch (InvalidKeyException e)
            {
                e.printStackTrace();
            }
            catch (InvalidAlgorithmParameterException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchPaddingException e)
            {
                e.printStackTrace();
            }
            catch (BadPaddingException e)
            {
                e.printStackTrace();
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (IllegalBlockSizeException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is invoked when the "decrypt" button is pressed.
     * Decrypts the text file that was provided
     *
     * @param event the event that was invoked when the button was pressed
     */
    @FXML
    void decryptHandler(ActionEvent event) throws FileNotFoundException
    {
        FileOutputStream output = new FileOutputStream(input_textfield.getText());
        if(input_textfield.getText() == null || input_textfield.getText().trim().isEmpty())
        {
            errorMessage("No input file","Please select a file to be Decrypted!!!");
        }
        else{
            try
            {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                SecretKeySpec skeySpec = new SecretKeySpec("moutsopoulos1234".getBytes("UTF-8"), "AES");
                String ivBytes = "1234567891123456";
                IvParameterSpec iv = new IvParameterSpec(ivBytes.getBytes("UTF-8"));

                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
                decrypt = cipher.doFinal(encrypt);
                output.write(decrypt);
                plain_text_area.appendText(new String(decrypt, "UTF-8"));
                decrypt_area.clear();
                decrypt_btn.setDisable(true);
                encrypt_btn.setDisable(false);
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            catch (InvalidKeyException e)
            {
                e.printStackTrace();
            }
            catch (InvalidAlgorithmParameterException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchPaddingException e)
            {
                e.printStackTrace();
            }
            catch (BadPaddingException e)
            {
                e.printStackTrace();
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (IllegalBlockSizeException e)
            {
                errorMessage("Cannot be decrypted", "The file you selected cannot be decrypted. It is not encrypted thus it cannot be decrypted");
                //e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    /**
     * This method is used to display error messages to the user
     *
     * @param title the header of the window
     * @param message the message that is displayed inside the body of the window
     */
    public void errorMessage(String title, String message)
    {
        Platform.runLater(()->
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Error message");
                alert.setHeaderText(title);
                alert.setContentText(message);

                alert.showAndWait();

            }

        );
    }//end of errorMessage




}//end of class mainWindowController

