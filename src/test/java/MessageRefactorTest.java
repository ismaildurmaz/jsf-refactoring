import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.intenum.common.Tracer;
import com.intenum.jsf.refactoring.MessageRefactor;

public class MessageRefactorTest {

	@Test
	public void test() throws Exception {
		MessageRefactor messageRefactor = new MessageRefactor();
		messageRefactor.setMessageTag("msg");
		messageRefactor.setPropertyFile(new File("C:\\Work\\projects\\herbaryum\\herbaryum\\src\\main\\resources\\common\\messages.properties"));
		messageRefactor.updatePropertyFile("Select", "select");
		File webAppFolder = new File(
				"C:\\Work\\projects\\herbaryum\\herbaryum\\src\\main\\webapp");
		messageRefactor.executeInFolder(webAppFolder, "plant.scientific_name", "plant.name.scientific", true);
	}

}
