package application;
	
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.stage.Stage;


public class Main extends Application {
	//Initialize all necessary variables
	final BarChart.Series<String, Number> series = new BarChart.Series<>();
    public ObservableList<XYChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();
    public List<Integer> arr = new ArrayList<>();
    static long time = -1;
	static int loopCounter = 0;
	static int comparisonCounter = 0;
	static int shiftCounter = 0;
	static int n;
	static int arrayID = 0;
	static String sType = "";
	
	//Initialize fxml components
	@FXML private BarChart<String, Number> arrayGraph;
    @FXML private Spinner<Integer> itemBox;
    @FXML private Spinner<Integer> searchTermBox;
    @FXML private Button btnGenerate;
    @FXML private ChoiceBox<String> sortBox;
    @FXML private Button btnSort;
    @FXML private Button btnReset;
    @FXML private ChoiceBox<String> searchBox;
    @FXML private Button btnSearch;
    @FXML private ProgressBar progressBar;
    @FXML private TableView table;
    
	@Override
	public void start(Stage primaryStage) {
		try {
			//Load and display stage
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Amazing Arrays!");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void initialize()
	{
		//On startup, eliminate graph ticks and numbers
		arrayGraph.setAnimated(true);
		arrayGraph.getYAxis().setTickLabelsVisible(false);
	    arrayGraph.getYAxis().setOpacity(0);
	    arrayGraph.getXAxis().setTickLabelsVisible(false);
	    arrayGraph.getXAxis().setOpacity(0);
	    barChartData.add(series);
    	arrayGraph.setData(barChartData);
    	
    	//Prepare number spinners
    	SpinnerValueFactory<Integer> itemValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 300, 100);
    	itemBox.setValueFactory(itemValueFactory);
    	SpinnerValueFactory<Integer> itemValueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 100);
    	searchTermBox.setValueFactory(itemValueFactory2);
    	
    	//Fill the sort box with options
    	sortBox.getItems().add(0, "Selection");
    	sortBox.getItems().add(1, "Bubble");
    	sortBox.getItems().add(2, "Insertion");
    	sortBox.getItems().add(3, "Quick");
    	
    	//Fill the search box with only linear option for now
    	searchBox.getItems().add(0, "Linear");
    	
    	//Disable some buttons for user check
    	btnSearch.setDisable(true);
    	btnSort.setDisable(true);
    	btnReset.setDisable(true);
	}

    @FXML
    void generate(){
    	//Get the item box value and use that as variable
    	n = itemBox.getValue();
    	
    	//Loop and add random numbers to both the array and chart
    	for (int i = 0; i < n; i++)
    	{
    		arr.add(i, (int)(Math.random() * 1000));
    		series.getData().add(new XYChart.Data<>(Integer.toString(i), (int)(Math.random() * 1000)));
    	}
    	
    	//Enable and disable some buttons
    	btnGenerate.setDisable(true);
    	btnSearch.setDisable(false);
    	btnSort.setDisable(false);
    	btnReset.setDisable(false);
    	sortBox.getSelectionModel().selectFirst();
    	searchBox.getSelectionModel().selectFirst();
    }

    @FXML
    void reset() {
    	//Clear all arrays and bar graph data
    	arr.clear();
    	series.getData().clear();
    	barChartData.clear();
    	arrayGraph.getData().clear();
    	barChartData.add(series);
    	arrayGraph.setData(barChartData);
    	
    	//Change array ID
    	arrayID++;
    	
    	//Enable and disable some buttons. Reset the choice boxes
    	btnSort.setDisable(true);
    	btnSearch.setDisable(true);
    	btnGenerate.setDisable(false);
    	btnReset.setDisable(true);
    	progressBar.setProgress(0);
    	searchBox.getItems().clear();
    	searchBox.getItems().add(0, "Linear");
    	searchBox.getSelectionModel().selectFirst();
    }

    @FXML
    void search() {
    	//Update the progress bar
    	progressBar.setProgress(0);
    	updateProgress();
    	
    	//Get search term from the box
    	int searchTerm = searchTermBox.getValue();
    	
    	//Initialize search index and timer
    	int searchIndex = -1;
    	time = System.currentTimeMillis();
    	
    	//Based on what the user wants, perform the type of search
    	if (searchBox.getValue() == "Linear")
    	{
    		if (btnSort.isDisabled())
    		{
    			searchIndex = linearSearch(arr, searchTerm);
    		}
    		else if (!btnSort.isDisabled())
    		{
    			searchIndex = LinearSearch(arr, searchTerm);
    		}
    	}
    	else if (searchBox.getValue() == "Binary")
    	{
    		searchIndex = binarySearch(arr, 0, arr.size() - 1, searchTerm);
    	}
    	
    	//Update time
    	time = System.currentTimeMillis() - time;
    	
    	//If the search term was not found, display a message saying it was not found
    	if (searchIndex == -1)
    	{
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("Warning");
    		alert.setHeaderText("Term Not Found");
    		alert.setContentText("Your search term was not found!");
    		alert.showAndWait();
    	}
    	//If it was found, indicate which index and display message
    	else
    	{
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Term Found");
    		alert.setHeaderText(null);
    		alert.setContentText("Your search term was found at index " + searchIndex);
    		alert.showAndWait();
    	}
    	
    	//Change the info variables and add it to the table. Reset the info
    	sType = searchBox.getValue() + " Search";
    	table.getItems().add(new SortInfo());
    	resetCounters();
    }

    @FXML
    void sort() {
    	//Update the progress bar
    	progressBar.setProgress(0);
    	updateProgress();
    	
    	//Start timer
    	time = System.currentTimeMillis();
    	
    	//Sort the array based on what the user wants
    	if (sortBox.getValue() == "Selection")
    	{
    		selectionSort(arr);
    	}
    	else if (sortBox.getValue() == "Bubble")
    	{
    		bubbleSort(arr);
    	}
    	else if (sortBox.getValue() == "Insertion")
    	{
    		insertionSort(arr);
    	}
    	else if (sortBox.getValue() == "Quick")
    	{
    		quickSort(arr, 0, arr.size()-1);
    	}
    	
    	//End timer
    	time = System.currentTimeMillis() - time;
    	
    	//Add info to table and reset the info variables
    	sType = searchBox.getValue() + " Sort";
    	table.getItems().add(new SortInfo());
    	resetCounters();
    	
    	//Update the graph and disable some buttons; add a binary option to the searchbox as well
    	updateArr(arr);
    	btnSort.setDisable(true);
    	searchBox.getItems().add(1, "Binary");
    }
    
    public void updateArr(List<Integer> arr)
    {
    	//Initialize size array
    	int n = arr.size();
    	
    	//Update the bar graph
    	series.getData().clear();
    	for (int i = 0; i < n; i++)
    	{
    		series.getData().add(new XYChart.Data<>(Integer.toString(i), arr.get(i)));
    	}
    }
    
    //Method for selection sort
    public static void selectionSort(List<Integer> data) {
		int smallest;
	    for (int i = 0; i < data.size() - 1; i++) {
	    	smallest = i;
	    	loopCounter++;
	    	//see if there is a smaller number further in the array
	    	for (int index = i + 1; index < data.size(); index++) 
	    	{
	    		loopCounter++;
	    		comparisonCounter++;
	    		if (data.get(index) < data.get(smallest))
	    		{
	    			shiftCounter++;
	    			swap(data, smallest, index);
	    		}
	    	}
	    }
	}
	
    //Method for bubble sort
	public static void bubbleSort(List<Integer> data) {
		//Loop to control number of passes
		for (int pass = 1; pass < data.size(); pass++) 
		{
			loopCounter++;
			//Loop to control # of comparisons for length of array-1
			for (int element=0; element<data.size()-1; element++) 
			{
				loopCounter++;
				//compare side-by-side elements and swap them if
				//first element is greater than second element
				comparisonCounter++;
				if (data.get(element) > data.get(element + 1))
				{
					swap(data, element, element + 1);  //call swap method
				}
			}
		}
	}
	
	//Method for insertion sort
	public static void insertionSort(List<Integer> data) {
		int insert;

		for (int next = 1; next < data.size(); next++) {
			loopCounter++;
			insert = data.get(next);
			int moveItem = next;
			
			while (moveItem > 0 && data.get(moveItem - 1) > insert) {
				loopCounter++;
				data.set(moveItem, data.get(moveItem - 1));
				moveItem--;
			}
			shiftCounter++;
			data.set(moveItem, insert);
		}
	}
	
	//Method for quick sort
	public static void quickSort(List<Integer> data, int low, int high) {
		int partitionLoc;
		loopCounter++;
		if (low < high) {
			comparisonCounter++;
			partitionLoc = partition(data, low, high);
			quickSort(data, low, partitionLoc - 1);
			quickSort(data, partitionLoc + 1, high);
		}
	}
	
	//Inner method used for quick sort
	public static int partition(List<Integer> data2, int left, int right) {
		shiftCounter++;
		
		boolean moveLeft = true;
		int separator = data2.get(left);

		while (left < right) {
			if (moveLeft == true) {
				while ((data2.get(right) >= separator) && (left < right)) {
					right--;
				}
				data2.set(left, data2.get(right));
				moveLeft = false;
			} else {
				while ((data2.get(left) <= separator) && (left < right)) {
					left++;
				}
				data2.set(right, data2.get(left));
				moveLeft = true;
			}
		}
		data2.set(left, separator);
		return left;
	}
	
	//Swap method used in some of the sorts
	public static void swap(List<Integer> array2, int first, int second) 
	{
		shiftCounter++;
		int hold = array2.get(first);
		array2.set(first, array2.get(second));
		array2.set(second, hold);
	}
	
	//Method for linear search, sorted
	public static int linearSearch(List<Integer> myArray, int V){
		int k;
		for(k = 0; k < myArray.size(); k++)
		{
			comparisonCounter++;
			if(myArray.get(k) == V) 
			{
				return k;
			}
			comparisonCounter++;
			if(V > myArray.get(k))
			{
				//Gone too far on the list so V exist
				return -1;
			}
			loopCounter++;
		}
		return -1;
	}
	
	//Method for linear search, unsorted
	public static int LinearSearch(List<Integer> myArray, int V)
	{
		int k;
		for(k = 0; k < myArray.size(); k++)
		{
			comparisonCounter++;
			if(myArray.get(k) == V) 
			{
			      return k;
			}
			loopCounter++;
		}
		return -1;
	}
	
	//Method for binary search
	public static int binarySearch(List<Integer> myArray, int l, int r, int x) 
    { 
		loopCounter++;
		comparisonCounter++;
        if (r >= l) { 
            int mid = l + (r - l) / 2;
            
            comparisonCounter++;
            if (myArray.get(mid) == x) 
                return mid; 
  
            comparisonCounter++;
            if (myArray.get(mid) > x) 
                return binarySearch(myArray, l, mid - 1, x); 
            
            return binarySearch(myArray, mid + 1, r, x); 
        }
        return -1; 
    } 
	
	//Method to update the progress bar
	public void updateProgress()
	{
		Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        		{
        			@Override
        			public void run()
        			{
        				if (progressBar.getProgress() < 1)
        				{
        					progressBar.setProgress(progressBar.getProgress() + 0.01);
        				}
        				else
        				{
        					timer.cancel();
        				}
        			}
        		},0,2);  
	}
	
	//Reset info counter variables
	public static void resetCounters()
	{
		loopCounter = 0;
		comparisonCounter = 0;
		shiftCounter = 0;
	}
	
	@FXML
    void exit() {
		//Exit the application
    	System.exit(0);
    }
}
