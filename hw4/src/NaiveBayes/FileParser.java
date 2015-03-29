import java.util.*;
import java.io.*;

public class FileParser
{
	public static void
	ReadData(SparseMatrix data, String fileDir, Set<String> voc) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileDir))) {
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				// Each line: <docID> <termID> <freq>
				String[] tokens = currentLine.split(" ");
				data.set(tokens[0], tokens[1], Double.parseDouble(tokens[2]));
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		data.update(voc);

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

		return topics.size();
	}

	public static Set<String>
	ReadVocabulary(String fileDir) {
		Set<String> res = new HashSet<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileDir))) {
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				res.add(currentLine);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return res;
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
