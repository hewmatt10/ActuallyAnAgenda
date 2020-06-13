import com.google.api.core.*;
import com.google.auth.oauth2.*;
import com.google.cloud.firestore.*;
import com.google.api.client.json.jackson2.*;

import com.google.cloud.firestore.*;
import com.google.firebase.*;
import com.google.firebase.cloud.*;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class GetInfoFromDatabase {
	Firestore db;
	public GetInfoFromDatabase() throws Exception {
		setup();
	}
	public void setup() throws IOException
	{
		FileInputStream serviceAccount = new FileInputStream("C:\\Users\\cotbe\\Documents\\ActuallyAnAgenda\\serviceAccount.json");

		// Initialize the app with a service account, granting admin privileges
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://actuallyanagenda.firebaseio.com/")
				.build();
		FirebaseApp.initializeApp(options);

		// As an admin, the app has access to read and write all data, regardless of Security Rules
		DatabaseReference ref = FirebaseDatabase.getInstance()
				.getReference("restricted_access/secret_document");
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Object document = dataSnapshot.getValue();
				System.out.println(document);
			}

			@Override
			public void onCancelled(DatabaseError error) {
			}
		});
	}

	public ArrayList<Task> queryAllTasks(String userID) throws Exception
	{
		//output ArrayList
		ArrayList<Task> taskList = new ArrayList<>();
		// asynchronously retrieve all users
		ApiFuture<QuerySnapshot> query = db.collection(userID).get();
		// ...
		// query.get() blocks on response
		QuerySnapshot querySnapshot = query.get();
		List<QueryDocumentSnapshot> allTasks = querySnapshot.getDocuments(); //documents
		for(QueryDocumentSnapshot curTask: allTasks)
		{
			Date dueDate = new java.util.Date((long) (double) curTask.getDouble("due") * 1000);
			double percentage = curTask.getDouble("per") / 100;
			long estimatedTimeRequiredInSeconds = (long) (double) curTask.getDouble("timeSecs");
			byte priority = (byte) (double) curTask.getDouble("timeSecs");
			String name = curTask.getString("name");
			taskList.add(new Task(dueDate, estimatedTimeRequiredInSeconds, priority, name));
		}
		return taskList;
	}

	public ArrayList<Task> getActiveTasks(String userID)throws Exception
	{
		ArrayList<Task> taskList = new ArrayList<>();

		ApiFuture<QuerySnapshot> query = db.collection(userID).get();
		QuerySnapshot querySnapshot = query.get();
		List<QueryDocumentSnapshot> allTasks = querySnapshot.getDocuments(); //documents

		for(QueryDocumentSnapshot curTask: allTasks)
		{
			Date dueDate = new java.util.Date((long) (double) curTask.getDouble("due") * 1000);
			double percentage = curTask.getDouble("per") / 100;
			long estimatedTimeRequiredInSeconds = (long) (double) curTask.getDouble("timeSecs");
			byte priority = (byte) (double) curTask.getDouble("timeSecs");
			String name = curTask.getString("name");
			taskList.add(new Task(dueDate, estimatedTimeRequiredInSeconds, priority, name));
		}
		return taskList;
	}

	public void update(ArrayList<Task> taskList, String userID)throws Exception
	{
		for(Task curTask: taskList)
		{
			// Create a Map to store the data we want to set
			Map<String, Object> taskData = new HashMap<>();
			taskData.put("due", curTask.getComplete().getTime());
			taskData.put("per", curTask.getPercentDone());
			taskData.put("timeSecs", curTask.getTime());
			taskData.put("priority", curTask.getPriority());
			taskData.put("name", curTask.getName());
			ApiFuture<WriteResult> future = db.collection(userID).document(curTask.getName()).set(taskData);
			System.out.println("Update time for " + curTask.getName() + ": " + future.get().getUpdateTime());
		}
	}
}