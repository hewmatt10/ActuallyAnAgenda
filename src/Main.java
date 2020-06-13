import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.util.*;

public class Main
{
	Firestore db;

	public static void main(String[] args)throws Exception
	{
		GetInfoFromDatabase gifb = new GetInfoFromDatabase();
		ArrayList<Task> test = gifb.queryAllTasks("wcGsw8HnzDZNSbS0SEKOyBSGUIQ2");
		for(Task t: test)
		{
			System.out.println(t);
		}
		System.out.println("done");
	}
}

