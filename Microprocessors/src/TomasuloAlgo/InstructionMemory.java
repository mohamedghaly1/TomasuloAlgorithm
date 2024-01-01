package TomasuloAlgo;

import java.io.BufferedReader;
import java.io.FileReader;

public class InstructionMemory {
	
	private static final String Program_File = "program.txt";
	 String[] Instructions = new String[2048];
	    
	    
	    
	    public void loadInstruction(String filePath) throws Exception {
	    	boolean Itype,Rtype,AddOP,Fr,Ir;
	    	
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line = br.readLine();
			String[] content = new String[4];
			while (line != null) {
				Itype=false;Rtype=false;AddOP=false;Fr=false;Ir=false;
				content = line.split("\\s|,\\s*|(?<=\\w+:)");
				if(content.length>4) throw new Exception("Assembler error make sure to write the assembly in the correct way");
				int p=0;
				if(content[p].charAt(content[p].length()-1)==':')p++;
				switch(content[p]) {
				case"ADD.D" :Rtype=true;Fr=true;break;
				case"SUB.D" :Rtype=true;Fr=true;break;
				case"MUL.D" :Rtype=true;Fr=true;break;
				case"DIV.D" :Rtype=true;Fr=true;break;
				case"DADD" :Rtype=true;Ir=true;break;
				case"ADDI" :Itype=true;Ir=true;break;
				case"SUBI" :Itype=true;Ir=true;break;
				case"L.D" :Itype=true;AddOP=true;Fr=true;break;
				case"S.D" :Itype=true;AddOP=true;Fr=true;break;
				case"BNEZ":Ir=true;break;
				default:throw new Exception("Invalid operation name");
				}
				
				if(Fr && (Integer.parseInt(content[p+1].substring(1))>31 || Integer.parseInt(content[p+1].substring(1))<0 || content[p+1].charAt(0)!='F' )) {
					throw new Exception("Invalid register name");
				}
				if(Ir && (Integer.parseInt(content[p+1].substring(1))>31 || Integer.parseInt(content[p+1].substring(1))<0 || content[p+1].charAt(0)!='R' )) 
					throw new Exception("Invalid register name");
			
				if(Rtype) {
					if(Fr) {
						if(Integer.parseInt(content[p+2].substring(1))>31 || Integer.parseInt(content[p+2].substring(1))<0 || content[p+2].charAt(0)!='F' ) 
							throw new Exception("Invalid register name");
						if(Integer.parseInt(content[p+3].substring(1))>31 || Integer.parseInt(content[p+3].substring(1))<0 || content[p+3].charAt(0)!='F' )
							throw new Exception("Invalid register name");
					}
					else if(Ir) {
						if(Integer.parseInt(content[p+2].substring(1))>31 || Integer.parseInt(content[p+2].substring(1))<0 || content[p+2].charAt(0)!='R' )
							throw new Exception("Invalid register name");
						if(Integer.parseInt(content[p+3].substring(1))>31 || Integer.parseInt(content[p+3].substring(1))<0 || content[p+3].charAt(0)!='R' )
							throw new Exception("Invalid register name");
					}
						}
				if(Itype) {
					if(AddOP) {
						if(Integer.parseInt(content[p+2])>2048 || Integer.parseInt(content[p+2])<0)throw new Exception("The immediate value is incorrectly written.");
					}
					else {
						if(Fr) {
							if(Integer.parseInt(content[p+2].substring(1))>31 || Integer.parseInt(content[p+2].substring(1))<0 || content[p+2].charAt(0)!='F' ) 
								throw new Exception("Invalid register name");
						}
						else if (Ir) {
							if(Integer.parseInt(content[p+2].substring(1))>31 || Integer.parseInt(content[p+2].substring(1))<0 || content[p+2].charAt(0)!='R' ) 
								throw new Exception("Invalid register name");
						}
					}
				}
				
				for(int i=0;i<Instructions.length;i++) {
					if(Instructions[i]==null) {Instructions[i]=line;break;}
				}
				line = br.readLine();
					
			}
			
	    }
	    

	    
	    public int size() {
	    	int size = 0;
	    	for (int i = 0; i < Instructions.length; i++) {
	    	    if (Instructions[i] != null) {
	    	        size++;
	    	    }
	    	    else break;
	    	    }
	    	return size;
	    }
	    
	    public static void main(String[] args) throws Exception {
	    	InstructionMemory im = new InstructionMemory();
	    	im.loadInstruction(Program_File);
			for (String x : im.Instructions) {
				if(x==null)break;
				System.out.println(x);
			}
	    	System.out.println(im.size());
		}
	    

}
