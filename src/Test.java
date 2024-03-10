import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Scanner;

public class Test {
        private static final String URL_USERS = "https://jsonplaceholder.typicode.com/users";
        private static final String URL_COMMENTS = "https://jsonplaceholder.typicode.com/posts/";

        public static void main(String[] args) throws IOException {

            //Завдання 1
            System.out.println("=========================EXERCISE 1=========================");
            createNewObject();
            updateObject();
            deleteObject();
            getObjects();
            getObjectById();
            getObjectByUsername();

            //Завдання 2
            System.out.println("\n=========================EXERCISE 2=========================");
            getComments();

            System.out.println("\n=========================EXERCISE 3=========================");
            getTodos();
        }

    private static void createNewObject() throws IOException {

        URL url = new URL(URL_USERS);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        OutputStream os = connection.getOutputStream();
        os.write(Files.readAllBytes(new File("test.json").toPath()));
        os.flush();
        os.close();

        int responseCode = connection.getResponseCode();
        System.out.println("POST response code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            System.out.println(response.toString());
        } else {
            System.out.println("Error with POST request");
        }

        connection.disconnect();
    }

    public static void updateObject() throws IOException {

        URL url = new URL(URL_USERS + "/1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        OutputStream os = connection.getOutputStream();
        os.write(Files.readAllBytes(new File("test.json").toPath()));
        os.flush();
        os.close();

        int responseCode = connection.getResponseCode();
        System.out.println("\nPUT response code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            System.out.println(response.toString());
        } else {
            System.out.println("Error with PUT request");
        }

        connection.disconnect();
    }

    public static void deleteObject() throws IOException {

        URL url = new URL(URL_USERS + "/10");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        System.out.println("\nDELETE response code: " + responseCode);

        if(responseCode != HttpURLConnection.HTTP_OK) {
            System.out.println("Error with DELETE request");
        }

        connection.disconnect();
    }

    private static void getObjects() throws IOException {

        URL url = new URL(URL_USERS);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        System.out.println("\nGET response code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            System.out.println(response.toString());
        } else {
            System.out.println("Error with GET request");
        }

        connection.disconnect();
    }

    public static void getObjectById() throws IOException {

        System.out.print("\nEnter user id: ");
        Scanner s = new Scanner(System.in);
        int id = s.nextInt();

        URL url = new URL(URL_USERS + "/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        System.out.println("GET response code: " + responseCode);

        if(responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            String line;
            while((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            System.out.println(response.toString());
        } else {
            System.out.println("Error with GET request");
        }

        connection.disconnect();
    }

    public static void getObjectByUsername() throws IOException {

        System.out.print("\nEnter user username: ");
        Scanner s = new Scanner(System.in);
        String username = s.nextLine();

        URL url = new URL(URL_USERS + "?username=" + username);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        System.out.println("GET response code: " + responseCode);

        if(responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            String line;
            while((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            System.out.println(response.toString());
        } else {
            System.out.println("Error with GET request");
        }

        connection.disconnect();
    }

    public static void getComments() throws IOException {

        System.out.print("Enter user id for getting comments: ");
        Scanner s = new Scanner(System.in);
        int userId = s.nextInt();

        URL urlForPosts = new URL(URL_USERS + "/" + userId + "/posts");
        HttpURLConnection connectionForPosts = (HttpURLConnection) urlForPosts.openConnection();
        connectionForPosts.setRequestMethod("GET");

        int responseCodeForPosts = connectionForPosts.getResponseCode();

        int lastPostId = 0;
        if(responseCodeForPosts == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connectionForPosts.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                if(line.contains("\"id\":")) {
                    lastPostId++;
                }
            }
            reader.close();
        } else {
            System.out.println("Error with GET request");
        }

        connectionForPosts.disconnect();

        URL urlForComments = new URL(URL_COMMENTS + lastPostId + "/comments");
        HttpURLConnection connectionForComments = (HttpURLConnection) urlForComments.openConnection();
        connectionForComments.setRequestMethod("GET");

        int responseCodeForComments = connectionForComments.getResponseCode();

        if(responseCodeForComments == HttpURLConnection.HTTP_OK) {
            System.out.println("\nComments for post " + lastPostId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connectionForComments.getInputStream()));
            StringBuffer response = new StringBuffer();
            String line;
            while((line = reader.readLine()) != null) {
                response.append(line);
            }

            String fileName = "user-" + userId + "-post-" + lastPostId + "-comments.json";
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(response.toString());
            fileWriter.close();

            reader.close();
            System.out.println(response.toString());
        } else {
            System.out.println("Error with GET request");
        }

        connectionForComments.disconnect();
    }

    public static void getTodos() throws IOException {

        System.out.print("Enter user id for getting duties: ");
        Scanner s = new Scanner(System.in);
        int userId = s.nextInt();

        URL url = new URL(URL_USERS + "/" + userId + "/todos?completed=false");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            String line;
            while((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            System.out.println(response.toString());
        } else {
            System.out.println("Error with GET request");
        }
    }
}
