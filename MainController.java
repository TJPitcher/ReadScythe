//ReadScythe MK. I prototype. please excuse the fact that it sucks so bad

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.util.Scanner;
import java.io.*;

public class MainController {

    @FXML
    private Button BtnAdd;

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnDisplay;

    @FXML
    private Button BtnSearch;

    @FXML
    private Button BtnUpdate;

    @FXML
    private CheckBox ChkRead;

    @FXML
    private CheckBox ChkTBR;

    @FXML
    private Label LblList;

    @FXML
    private TextArea TareaReadingList;

    @FXML
    private TextArea TareaTags;

    @FXML
    private TextField TxtAuthor;

    @FXML
    private TextField TxtTitle;
	
	File listFile=new File("data/list.mp3");
	
	//yes. these have to be 2 booleans native to the controller for this to work. dont even ask
	private boolean haveRead = false;
	private boolean toBeRead = false;
	
	void clearText() {
		TxtTitle.setText("");
		TxtAuthor.setText("");
		TareaTags.setText("");
	}
	
	void showList() {
		try {
			TareaReadingList.setText("");
			Scanner listRead=new Scanner(listFile);
			while (listRead.hasNext()) {
				String s = listRead.nextLine();
				Scanner tempScn = new Scanner(s);
				tempScn.useDelimiter(";");
				String title = tempScn.next();
				String author = tempScn.next();
				boolean readState = tempScn.nextBoolean();
				String tags = tempScn.next();
				
				System.out.println("showList(): "+title+author+readState+tags);
				System.out.println("showList(): "+s);
				
				String readString;
				if (readState) {readString = "Have read";}
				else {readString = "To be read";}
				
				TareaReadingList.appendText(title+" by "+author+": "+readString+" | "+tags+"\n");
			}
			listRead.close();
		} catch (Exception e) {System.out.println(e.getMessage());}
	}
	
	@FXML
	void initialize() {
		showList();
		System.out.println("initialize(): lol");
		TareaReadingList.setEditable(false);
		
	}
	
    @FXML
    void Add(ActionEvent event) {
		try {
			String title = TxtTitle.getText();
			String author = TxtAuthor.getText();
			String tags = TareaTags.getText();
			boolean readState;
			if (title=="") {throw new Exception("Title cannot be blank");}
			if (author=="") {throw new Exception("Author cannot be blank");}
			if (tags=="") {tags=" ";}
			
			if (!haveRead) {//i tried doing this more elegantly. it cant be done
				if (toBeRead) {
					readState = false;
				} else {throw new Exception("Have read and To Be Read cannot both be false");}
			} else {readState = true;}
			
			PrintWriter pw1=new PrintWriter(new BufferedWriter(new FileWriter(listFile,true)));
			pw1.println(title+";"+author+";"+readState+";"+tags);
			pw1.close();
			
			clearText();
			showList();
		} catch (Exception e) {
			TareaReadingList.setText("ERROR: " + e.getMessage());
		}
    }

    @FXML
    void Update(ActionEvent event) {
		
    }

    @FXML
    void Delete(ActionEvent event) {//reads entire list, excempts exceptions, filewrites list.
		try {
			//TODO this whole fkn thing
			String title = TxtTitle.getText();
			String author = TxtAuthor.getText();
			
			Scanner listRead = new Scanner(listFile);
			String newList = "";
			boolean allowed;
			while (listRead.hasNext()) {
				allowed = true;
				String line = listRead.nextLine();
				
				Scanner tmpScn = new Scanner(line);
				tmpScn.useDelimiter(";");
				String scanTitle = tmpScn.next();
				String scanAuthor = tmpScn.next();
				tmpScn.close();
				
				if ((title.equals(scanTitle)) && (author.equals(scanAuthor))) {allowed = false;}
				System.out.println("Delete(): "+title+" "+scanTitle+" "+author+" "+scanAuthor+" "+allowed);
				if (allowed) {newList = newList+line+"\n";}
				System.out.println(newList);
			}
			FileWriter fw = new FileWriter(listFile);
			fw.write(newList);
			fw.close();
			showList();
		} catch (Exception e) {
			System.out.println("ERROR in Delete: "+e.getMessage());
		}
    }

    @FXML
    void Search(ActionEvent event) {
		
    }

    @FXML
    void HaveReadList(ActionEvent event) {//direct your confusion to line 53
		if (ChkRead.isSelected()) {
			ChkTBR.setSelected(false);
			haveRead = true;
			toBeRead = false;
		} else {
			ChkTBR.setSelected(true);
			haveRead = false;
			toBeRead = true;
		}
    }

    @FXML
    void TBRList(ActionEvent event) {//ditto
		if (ChkTBR.isSelected()) {
			ChkRead.setSelected(false);
			haveRead = false;
			toBeRead = true;
		} else {
			ChkRead.setSelected(true);
			haveRead = true;
			toBeRead = false;
		}
		
    }
	
	@FXML
    void DisplayList(ActionEvent event) {
		showList();
    }



}
