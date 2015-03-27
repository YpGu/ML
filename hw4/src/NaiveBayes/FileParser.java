import java.util.*;
import java.io.*;

public class FileParser
{
	public static void
	ReadData(SparseMatrix data, String fileDir) {
		Set<String> terms = new HashSet<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileDir))) {
			int lineID = 0;
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				// Each line: <docID> <termID> <freq>
				String[] tokens = currentLine.split(" ");
				terms.add(tokens[1]);
				data.set(tokens[0], tokens[1], Double.parseDouble(tokens[2]));
				lineID += 1;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		data.update();

		return;
	}

	public static int 
	ReadLabel(SparseMatrix data, String fileDir) {
		Set<Integer> topics = new HashSet<Integer>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileDir))) {
			String currentLine;
			int lineID = 1;
			while ((currentLine = br.readLine()) != null) {
				// Each line: <class>
				String docID = Integer.toString(lineID);
				int nClass = Integer.parseInt(currentLine)-1;			// classes are 1-K, we store them as 0~(K-1)
				topics.add(nClass);
				data.setLabel(docID, nClass);
				lineID += 1;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		data.update();

		return topics.size();
	}

	public static int
	ReadVocabularySize(String fileDir) {
		int lineID = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(fileDir))) {
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				lineID += 1;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return lineID;
	}
}
