package LibraryManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookDto {

    private int id;
    private String name;
    private String type;
    private String language;
    private String status;
    private String author;
    private AdminDto admin;


    public String getLAuthor() {
        return author;
    }
}

