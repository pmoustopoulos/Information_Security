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


/**
 * This is the mainWindowController that is connected with the fxml file
 */
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
    private TextField input_textfield;

    @FXML
    private Button search_btn;

    @FXML
    private Button help_btn;

    /**
     * The array of bytes that holds the encrypted message
     */
    byte[] encrypt;

    /**
     * The array of bytes that holds the decrypted message
     */
    byte[] decrypt;

    /**
     * The array of bytes that holds the message
     */
    byte [] message;


    /**
     * This method is used to select the file from the computer.
     *
     * @param event the event that was invoked when the button was pressed
     * @throws FileNotFoundException thrown if the file is not found
     */
    @FXML
    void selectFileHandler(ActionEvent event) throws IOException
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("open files");
        //filters the selections to be only .txt files
        //Not used eventually in order to manipulate different types of files
//        FileChooser.ExtensionFilter extFilter =
//                new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
//        fileChooser.getExtensionFilters().add(extFilter);
        encrypt_btn.setDisable(false);
        encrypt_btn.setDisable(false);
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
            message = new byte[input.available()];
            int counter=0;
            try
            {
                while((i = input.read()) != -1)
                {
                    message[counter++] += (byte)i;
                }
            }
            catch(IOException e)
            {
                errorMessage("File unavailable","The file could not be selected or found");
                e.printStackTrace();
            }

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

        if(input_textfield.getText() == null || input_textfield.getText().trim().isEmpty())
        {
            errorMessage("No input file","Please select a file to be Encrypted!!!");
        }
        else
        {
            FileOutputStream output = new FileOutputStream(input_textfield.getText());
            try
            {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                SecretKeySpec skeySpec = new SecretKeySpec("wH@td0uWanT@gaIn".getBytes("UTF-8"), "AES");
                String ivBytes = "uwI11n3verP@sSM3";
                IvParameterSpec iv = new IvParameterSpec(ivBytes.getBytes("UTF-8"));
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

                encrypt = cipher.doFinal(message);

                output.write(encrypt);
                informUser("Encrypted");
                output.close();
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
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (IllegalBlockSizeException e)
            {
                errorMessage("Cannot be encrypted", "The file you selected cannot be encrypted because it is already encrypted");
            }
        }
    }//end of encryptHandler

    /**
     * This method is invoked when the "decrypt" button is pressed.
     * Decrypts the text file that was provided
     *
     * @param event the event that was invoked when the button was pressed
     */
    @FXML
    void decryptHandler(ActionEvent event) throws FileNotFoundException
    {

        if(input_textfield.getText() == null || input_textfield.getText().trim().isEmpty())
        {
            errorMessage("No input file","Please select a file to be Decrypted!!!");
        }
        else{
            try
            {
                FileOutputStream output = new FileOutputStream(input_textfield.getText());
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                SecretKeySpec skeySpec = new SecretKeySpec("wH@td0uWanT@gaIn".getBytes("UTF-8"), "AES");
                String ivBytes = "uwI11n3verP@sSM3";
                IvParameterSpec iv = new IvParameterSpec(ivBytes.getBytes("UTF-8"));

                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

                if(encrypt == null)
                {
                    decrypt = cipher.doFinal(message);
                }
                else
                {
                    decrypt = cipher.doFinal(encrypt);
                }

                output.write(decrypt);
                informUser("Decrypted");
                output.close();
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
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (IllegalBlockSizeException e)
            {
                errorMessage("Cannot be decrypted", "The file you selected cannot be decrypted. It is not encrypted thus it cannot be decrypted");
            }
        }
    }//end of decryptHandler

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

    /**
     * This method is used to create a pop-up window to inform
     * the user about their action
     *
     * @param action the action they made is displayed inside the window
     */
    public void informUser(String action)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(action + " status");
        alert.setHeaderText("The file was " + action + " successfully!");
        alert.setContentText(null);

        alert.showAndWait();
    }//end of informUser

    /**
     * This method is invoked when the help button is pressed.
     * A pop-up window will be displayed that will show how to use
     * the application
     *
     * @param event the event that was triggered  when the button was pressed
     */
    @FXML
    void helpHandler(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setHeaderText("How to use the application");
        alert.setContentText("This application is used to encrypt and decrypt files. In order to do that specific steps have to be followed.\n" +
                "The steps that have to be followed are:\n\n" +
                "1. Press the button with the three dots \"...\".\n\n" +
                "2. Select the file that you want to encrypt.\n\n" +
                "3. As soon as a file is selected, inside the textfield the path of the file will appear.\n\n" +
                "4. Then press the button \"Encrypt\" and a pop-up window will be displayed to inform you if the action was successfully accomplished.\n" +
                "    * If a file was not selected and any of the two buttons \"Encrypt\" or \"Decrypt\" are pressed then a pop-up window appears that will inform about the incorrect action.\n\n" +
                "5. In order to decrypt the file, the same steps have to be followed but instead of pressing the \"Encrypt\" \n" +
                "button, press the \"Decrypt\" button.\n\n" +
                "6. You cannot type the path inside the textfield. The path will appear only if you selected the file from the pop-up window!!!");

        alert.showAndWait();
    }//end of helpHandler

}//end of class mainWindowController

