package LibraryManagementSystem.controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import LibraryManagementSystem.dto.TransactionDto;
import LibraryManagementSystem.service.ServiceFactory;
import LibraryManagementSystem.service.custom.TransactionService;
import LibraryManagementSystem.util.Navigation;

import java.io.IOException;

public class UserBorrowedBooksBarFormController {

    @FXML
    private Label lblAmount;

    @FXML
    private Label lblBorrowedDate;

    @FXML
    private Label lblDueDate;

    @FXML
    private Label lblId;

    @FXML
    private Label lblUserID;

    @FXML
    private Label lblReturn;

    @FXML
    private ImageView returnBtnImg;

    @FXML
    private Pane returnBtnPane;

    public static int transactionId;

    TransactionService transactionService =
            (TransactionService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceTypes.TRANSACTION);

    @FXML
    void btnReturnOnAction(ActionEvent event) throws IOException {
        transactionId = Integer.parseInt(lblId.getText());
        Navigation.imgPopUpBackground("userReturnBookConfirmPopUpForm.fxml");
    }

    @FXML
    void btnReturnOnMouseEntered(MouseEvent event) {

    }

    @FXML
    void btnReturnOnMouseExited(MouseEvent event) {

    }

    public void setData(int id) {
        TransactionDto transactionDto = transactionService.getTransactionData(id);

        lblId.setText(String.valueOf(transactionDto.getId()));
        lblAmount.setText("0" + transactionDto.getBookQty());
        lblDueDate.setText(transactionDto.getDueDate());
        lblUserID.setText(String.valueOf(transactionDto.getUser().getId()));

        String[] split = String.valueOf(transactionDto.getDateAndTime()).split(" ");
        lblBorrowedDate.setText(split[0]);
    }

}
