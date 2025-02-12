package LibraryManagementSystem.controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import LibraryManagementSystem.util.Navigation;

import java.io.IOException;

public class UserSettingsPopUpFormController {

    @FXML
    void btnChangeCredentialsOnAction(ActionEvent event) throws IOException {
        Navigation.closeUserSettingsPane();
        Navigation.imgPopUpBackground("userChangeCredentialsPopUpForm.fxml");
    }

    @FXML
    void btnDeleteAccountOnAction(ActionEvent event) throws IOException {
        Navigation.closeUserSettingsPane();
        Navigation.imgPopUpBackground("userDeleteConfirmationForm.fxml");
    }

}
