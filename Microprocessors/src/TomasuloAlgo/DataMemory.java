package TomasuloAlgo;

public class DataMemory {
	
	 float[] Data = new float[2048];
	    
	    public float readData(int address) {
			return Data[address];
	    	
	    }
	    public void writeData(int address,int value) {
			Data[address]=value;
	    	
	    }
	    public void writeData(int address, float value) {
			Data[address]=value;
	    	
	    }

}
