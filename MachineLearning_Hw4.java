import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;


/**
 * 
 * @author Kiran Bhat
 * Date 03/19/2015
 * Description: This program implements the Viterbi algorithm to compute the most likely state sequence given the observation
 * 				 sequence. Program assumes the input files are provided as described in the same order as in input(please see below)
 *				First the program computes the matrix by calculating the state probability of each state for the given observed sequence.
 *				Then the matrix is recursively called till we reach the first state.
 *Input: 2 files containing the model data and test data
 *		Model data: # of states (let’s say N)
					Initial state probabilities (N values here)
					Transition probabilities (This will contain N*N values in the transition matrix. The values are
					row-based.)
					# of output symbols (let’s say M)
					Output alphabet (M values here. They can be discrete numbers or strings for the observations)
					Output distributions (This will contain N*M values, M values for probability mass function for
					each state, one by one.)
		Test Data:observation sequence
*output: Prints out the most likely state sequence for the given observation sequence.
 */

public class MachineLearning_Hw4 {

	public MachineLearning_Hw4() {
		// TODO Auto-generated constructor stub
	}
	public static int MAX = 100;
	public static int noOfStates = 0;
	
	
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.out.println("You should give exactly 2 argumetns as input which are names of training data and test data files");
			System.exit(1);

		}
					
					
		String model=args[0];       
        String testFile=args[1];
        
        //Read the content of the files into respective variables.
        computePath(model, testFile);
		
	}
	
	public static  void computePath(String model, String testFile) throws IOException {

		// Read the input file.
		FileReader fileReaderModel = null;
		FileReader fileReaderTest = null;
		
		try {
			fileReaderModel = new FileReader(new File(model));
		} catch (FileNotFoundException e) {
		}
		
		try {
			fileReaderTest = new FileReader(new File(testFile));
		} catch (FileNotFoundException e) {
			
		}

        BufferedReader bufferReaderModel = new BufferedReader(fileReaderModel);
        BufferedReader bufferReaderTest = new BufferedReader(fileReaderTest);
        
		// If the file was successfully opened, parse the file
		//parse(bufferReaderModel);
		//computePath(bufferReaderTest);
	
	
		   String line=null;
		   
		   //while loop for each output sequence from test file
		   
			   noOfStates=Integer.parseInt(bufferReaderModel.readLine());
			   //Declare other variables
			   
			   double[] initialStateValues = new double[noOfStates];
				double[][] transitionMatrix =new double[noOfStates][noOfStates];
				int noOfOutputSymbols = 0;
				String outputSymbols[];
			   String[] temp = bufferReaderModel.readLine().split(" ");
			   //Store the initial probabilities as Double in initialState Values
			   for(int i =0; i < temp.length; i++)
			   {
				  // System.out.println("i is :" + i + "temp[i] is " + temp[i]+ "initialstate value[i]" + initialStateValues[i]);
				   initialStateValues[i] = Double.parseDouble(temp[i]);
			   }
			 //reading transition probabilities
			   String[] tempMatrix=bufferReaderModel.readLine().split(" "); 
			      //storing transition probabilities in a matrix
			        
			        int k=0;
			        for(int i=0;i<noOfStates;i++)	
			        {						
			        	for(int j=0;j<noOfStates;j++)
			        	{
			        		transitionMatrix[i][j]=Double.parseDouble(tempMatrix[k++]);
			        	}
			        }
			   noOfOutputSymbols = Integer.parseInt(bufferReaderModel.readLine());
			   double[][] outputProbability =new double[noOfStates][noOfOutputSymbols];
			   outputSymbols=bufferReaderModel.readLine().split(" ");
			   temp = null;
			   temp=bufferReaderModel.readLine().split(" ");
		        k=0;
		        //Store the values in the outputProbability matrix
		        for(int i=0;i<noOfStates;i++)
		        {
		        	for(int j=0;j<noOfOutputSymbols;j++)
		        	{
		        		//System.out.println("i, j, k" + i+ j + k);
		        		outputProbability[i][j]=Double.parseDouble(temp[k++]);
		        	}
		        }
		     bufferReaderModel.close();
	
	
		line = null;
		
		
		while((line =bufferReaderTest.readLine()) != null)
		{
			String tempLine = line;
			//String outputSequence = line.replace("\\s", "");
			String outputSequence = line.replaceAll("\\s","");
			double[] currentStateProbability = new double[noOfStates];
			
			int output = 0;
			
			//Compute the index for the first output sequence character
			for(int i=0;i<outputSymbols.length;i++)
	        {
	        	if(outputSymbols[i].charAt(0)== outputSequence.charAt(0))
	        	{
	        		output=i;
	        		break;
	        	}
	        }
			int maxProbabilityState = 0;
			double maxProbability = 0;
			//Find the maximum probability and the state it belongs to
			for(int i=0;i<noOfStates;i++)
	        {
	        	currentStateProbability[i]=initialStateValues[i]*outputProbability[i][output];
	        	if(currentStateProbability[i]>= (double)maxProbability)
	        	{
	        		maxProbabilityState = i;
	        		maxProbability = currentStateProbability[i];
	        	}
	        }
			int[][] outputMatrix = new int[noOfStates][outputSequence.length()];
			
			//First column of matrix represents the 
			for(int i=0;i<noOfStates;i++)
	        {
	        	outputMatrix[i][0]= maxProbabilityState;
	        }
			
			for(int i =1; i < outputSequence.length();i++)
			{
				//Find the output character in the output Sequence
				output=0;
	        	for(int j=0;j<outputSymbols.length;j++)
		        {
		        	if(outputSymbols[j].charAt(0)==outputSequence.charAt(i))
		        	{
		        		output=j;
		        		break;
		        	}
		        }
			
			
			
			double[] maxProbabilityOfState = new double[noOfStates];
			//Compute the maximun probability for each state and store the state wth max probability
			for(k=0;k<noOfStates;k++)
        	{
				int previousState = 0;
        		double tempMax=0;
        		for(int l=0;l<noOfStates;l++)
        		{
        			double tempProbability = currentStateProbability[l]*transitionMatrix[l][k]*outputProbability[k][output];
        			if(tempProbability>=tempMax)
        			{
        				previousState=l;
        				tempMax=tempProbability;
        			}
        		}
        		//System.out.println("Temp Max is " + tempMax);
        		maxProbabilityOfState[k]=tempMax;
        		outputMatrix[k][i]=previousState;
        	}
			currentStateProbability = maxProbabilityOfState.clone();
			/*for ( int z = 0; z <noOfStates; z++)
			{
				System.out.println("testing" + currentStateProbability[z]);
			}*/
			}
			
			
			
			//Find the state for the final output sequence
			double finalMax=0;
	        int finalState=0;
	        for(int i=0;i<noOfStates;i++)
	        {
	        	if(currentStateProbability[i]>=finalMax)
	        	{
	        		finalMax=currentStateProbability[i];
	        		finalState=i;
	        	}
	        }
	        int currentState =finalState;
	        //String s2=String.valueOf(cur_state);
	        StringBuilder printBuffer=new StringBuilder();
	        //Store the sequence of output Sequence in in the buffer
	        for(int i=outputSequence.length()-1; i>=0; i--)
	        {
	        	//Do not insert the arrow(->) symbol for the first state
	        	if(i==0)
	        	{
	        		printBuffer.insert(0, "S"+String.valueOf(currentState+1));
	        	}
	        	else
	        	{
	        	printBuffer.insert(0, "->S"+String.valueOf(currentState +1));
	        	currentState = outputMatrix[currentState][i];
	        	}
	        }
	        
	       //printing the final output
	       System.out.println("Given output sequence: "+ tempLine);
	       System.out.println("Most likely state sequence:");
	       System.out.println(printBuffer);
	        
		}
	
	
	}
}
