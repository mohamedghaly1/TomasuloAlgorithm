package TomasuloAlgo;

import java.util.Arrays;

public class Processor {
	private static final String Program_File = "program.txt";
	
	 int[]  IntegerRegister = new int[32];
	 String[]  CheckIntegerRegister = new String[32];
	 float[]  FloatingPointRegister = new float[32];
	 String[]  CheckFloatingPointRegister = new String[32];
	 
	 String[][]  AddSubReserv; //= new String[2][12];//0:Busy,1:op,2:vi,3:vk,4:qi,5:qk,6:Time,7:issued,8:Finished,9:fifo,10:finished recently,11:instruction
	 String[][]  MulDivReserv; //= new String[2][12];//0:Busy,1:op,2:vi,3:vk,4:qi,5:qk,6:Time,7:issued,8:Finished,9:fifo,10:finished recently,11:instruction
	 String[][]  LoadBuffer; //= new String[2][8];//0:Busy,1:address,2:Time,3:issued,4:Finished,5:fifo,6:finished recently,7:instruction
	 String[][]  StoreBuffer; //= new String[2][10];//0:Busy,1:address,2:v,3:q,4:Time,5:issued,6:Finished,7:fifo,8:finished recently,9:instruction
	 
	 boolean branched=false;
	 int Pc=0;
	 int FIFO=1;
	 String[] content = new String[4];
	 String AddDLatency;
	 String SubDLatency;
	 String MulDLatency;
	 String DivDLatency;
	 String DADDLatency;
	 String ADDILatency;
	 String SUBILatency;
	 String BenzLatency;
	 String LDLatency;
	 String SDLatency;
	 
	 InstructionMemory im = new InstructionMemory();
	 DataMemory dm =  new DataMemory();
	 int cycles=0;
	 
	 String status = "";

	 
	 public Processor(int add, int sub,int mul, int div , int dadd, int subi, int ld, int sd, int reserveA,int reserveM, int loadbuffer, int storebuffer) {
		 AddDLatency=add+"";
		 SubDLatency=sub+"";
		 MulDLatency=mul+"";
		 DivDLatency=div+"";
		 DADDLatency=add+"";
		 ADDILatency="1";
		 SUBILatency=subi+"";
		 BenzLatency="1";
		 LDLatency=ld+"";
		 SDLatency=sd+"";
		 AddSubReserv = new String[reserveA][12];
		 MulDivReserv = new String[reserveM][12];
		 LoadBuffer = new String[loadbuffer][8];
		 StoreBuffer = new String[storebuffer][10];
	 }
	 
	 
	 public void Issue() {
		 if(!branched && content[0]!=null) {
		 if(content[0].equals("ADD.D")) {
			 if(!isFull(AddSubReserv)) {
				 addToReserveBuffer(1);
			 }
		 }
		 if(content[0].equals("DADD")) {
			 if(!isFull(AddSubReserv)) {
				 addToReserveBuffer(1);
			 }
		 }
		 if(content[0].equals("SUB.D")) {
			 if(!isFull(AddSubReserv)) {
				 addToReserveBuffer(1);
			 }
		 }
		 if(content[0].equals("MUL.D")){
			 if(!isFull(MulDivReserv)) {
				 addToReserveBuffer(2);
			 }
		 }
		 if(content[0].equals("DIV.D")) {
			 if(!isFull(MulDivReserv)) {
				 addToReserveBuffer(2);
			 }
		 }
		 if(content[0].equals("ADDI")) {
			 if(!isFull(AddSubReserv)) {
				 addToReserveBuffer(3);
			 }

		 }
		 if(content[0].equals("SUBI")) {
			 if(!isFull(AddSubReserv)) {
				 addToReserveBuffer(3);
			 }

		 }
		 if(content[0].equals("BNEZ")) {
			 if(!isFull(AddSubReserv)) {
				 addToReserveBuffer(4);
			 }
		 }
		 if(content[0].equals("L.D")) {
			 if(!isFull(LoadBuffer)) {
				 addToReserveBuffer(5);
			 }
		 }
		 if(content[0].equals("S.D")) {
			 if(!isFull(StoreBuffer)) {
				 addToReserveBuffer(6);
			 }
		 }
		 }
		 
	 }
	 
	 
	 public void Execute() {
		 for (int i = 0; i < AddSubReserv.length; i++) {
			 if(AddSubReserv[i][0]!=null && AddSubReserv[i][8]==null) {
				 if(AddSubReserv[i][7]!=null)AddSubReserv[i][7]=null;
				 else if(AddSubReserv[i][4]==null && AddSubReserv[i][5]==null) {
					 AddSubReserv[i][6]=(Integer.parseInt(AddSubReserv[i][6])-1)+"";
					 if(AddSubReserv[i][6].equals("0")) {
						 AddSubReserv[i][8]="1";
						 AddSubReserv[i][10]="1";
						 status+=AddSubReserv[i][11]+" Execution completed at cycle "+(cycles+1)+"\n";
						 if(AddSubReserv[i][1].equals("ADD.D"))AddSubReserv[i][7]=(Float.parseFloat(AddSubReserv[i][2])+Float.parseFloat(AddSubReserv[i][3]))+"";
						 if(AddSubReserv[i][1].equals("SUB.D"))AddSubReserv[i][7]=(Float.parseFloat(AddSubReserv[i][2])-Float.parseFloat(AddSubReserv[i][3]))+"";
						 if(AddSubReserv[i][1].equals("ADDI"))AddSubReserv[i][7]=(Integer.parseInt(AddSubReserv[i][2])+Integer.parseInt(AddSubReserv[i][3]))+"";
						 if(AddSubReserv[i][1].equals("SUBI"))AddSubReserv[i][7]=(Integer.parseInt(AddSubReserv[i][2])-Integer.parseInt(AddSubReserv[i][3]))+"";
						 if(AddSubReserv[i][1].equals("DADD"))AddSubReserv[i][7]=(Integer.parseInt(AddSubReserv[i][2])+Integer.parseInt(AddSubReserv[i][3]))+"";
						 if(AddSubReserv[i][1].equals("BNEZ")) {
							 int oldpc=Pc;
							 AddSubReserv[i][7]=Integer.parseInt(AddSubReserv[i][2])!=0?Integer.parseInt(AddSubReserv[i][3])-1+"":oldpc+"";
						 }
					 }
					 }
				 }}
		 for (int i = 0; i < MulDivReserv.length; i++) {
			 if(MulDivReserv[i][0]!=null && MulDivReserv[i][8]==null) {
				 if(MulDivReserv[i][7]!=null)MulDivReserv[i][7]=null;
				 else if(MulDivReserv[i][4]==null && MulDivReserv[i][5]==null) {
					 MulDivReserv[i][6]=(Integer.parseInt(MulDivReserv[i][6])-1)+"";
					 if(MulDivReserv[i][6].equals("0")) {
						 MulDivReserv[i][8]="1";
						 MulDivReserv[i][10]="1";
						 status+=MulDivReserv[i][11]+" Execution completed at cycle "+(cycles+1)+"\n";
						 if(MulDivReserv[i][1].equals("MUL.D"))MulDivReserv[i][7]=(Float.parseFloat(MulDivReserv[i][2])*Float.parseFloat(MulDivReserv[i][3]))+"";
						 if(MulDivReserv[i][1].equals("DIV.D"))MulDivReserv[i][7]=(Float.parseFloat(MulDivReserv[i][2])/Float.parseFloat(MulDivReserv[i][3]))+"";
					 }
					 }
				 }}
		 for (int i = 0; i < LoadBuffer.length; i++) {
			 if(LoadBuffer[i][0]!=null&& LoadBuffer[i][4]==null) {
				 if(LoadBuffer[i][3]!=null)LoadBuffer[i][3]=null;
				 else {
					 LoadBuffer[i][2]=(Integer.parseInt(LoadBuffer[i][2])-1)+"";
					 if(LoadBuffer[i][2].equals("0")) {
						 status+=LoadBuffer[i][7]+" Execution completed at cycle "+(cycles+1)+"\n";
						 LoadBuffer[i][4]="1";
						 LoadBuffer[i][6]="1";
					 }
					 }
				 }}
		 for (int i = 0; i < StoreBuffer.length; i++) {
			 if(StoreBuffer[i][0]!=null && StoreBuffer[i][6]==null) {
				 if(StoreBuffer[i][5]!=null)StoreBuffer[i][5]=null;
				 else if(StoreBuffer[i][3]==null) {
					 StoreBuffer[i][4]=(Integer.parseInt(StoreBuffer[i][4])-1)+"";
					 if(StoreBuffer[i][4].equals("0")) {
						 status+=StoreBuffer[i][9]+" Execution completed at cycle "+(cycles+1)+"\n";
						 StoreBuffer[i][6]="1";
						 StoreBuffer[i][8]="1";
					 }
					 }
				 }}
		 }
	 
	 
	 public void writeBack() {
		 String tag="";String value="";int checkFIFO=10000;
		 
		 //getting the first instruction that wants to write
		 for (int i = 0; i < AddSubReserv.length; i++) {
			 if(AddSubReserv[i][0]!=null && AddSubReserv[i][8]!=null) {
				 if(AddSubReserv[i][10]!=null)AddSubReserv[i][10]=null;
				 else checkFIFO=checkFIFO>Integer.parseInt(AddSubReserv[i][9])?Integer.parseInt(AddSubReserv[i][9]):checkFIFO;
			 }
		 }
		 for (int i = 0; i < MulDivReserv.length; i++) {
			 if(MulDivReserv[i][0]!=null && MulDivReserv[i][8]!=null) {
				 if(MulDivReserv[i][10]!=null)MulDivReserv[i][10]=null;
				 else checkFIFO=checkFIFO>Integer.parseInt(MulDivReserv[i][9])?Integer.parseInt(MulDivReserv[i][9]):checkFIFO;
			 }
		 }
		 for (int i = 0; i < LoadBuffer.length; i++) {
			 if(LoadBuffer[i][0]!=null && LoadBuffer[i][4]!=null) {
				 if(LoadBuffer[i][6]!=null)LoadBuffer[i][6]=null;
				 else checkFIFO=checkFIFO>Integer.parseInt(LoadBuffer[i][5])?Integer.parseInt(LoadBuffer[i][5]):checkFIFO;
			 }
		 }
		 for (int i = 0; i < StoreBuffer.length; i++) {
			 if(StoreBuffer[i][0]!=null && StoreBuffer[i][6]!=null) {
				 if(StoreBuffer[i][8]!=null)StoreBuffer[i][8]=null;
				 else checkFIFO=checkFIFO>Integer.parseInt(StoreBuffer[i][7])?Integer.parseInt(StoreBuffer[i][7]):checkFIFO;
			 }
		}
		 
		 //getting the tag and the value for the instruction that wants to write
		 for (int i = 0; i < AddSubReserv.length; i++) {
			 if(AddSubReserv[i][0]!=null && AddSubReserv[i][8]!=null && checkFIFO==Integer.parseInt(AddSubReserv[i][9])) {
				 if(AddSubReserv[i][1].equals("BNEZ")) {Pc=Integer.parseInt(AddSubReserv[i][7]);branched=false;}
				 tag = "A"+(i+1);value =AddSubReserv[i][7];
				 status+=AddSubReserv[i][11]+" Written at cycle "+(cycles+1)+"\n";
				 AddSubReserv[i][0]=null; AddSubReserv[i][1]=null; AddSubReserv[i][2]=null; AddSubReserv[i][3]=null; AddSubReserv[i][4]=null;
				 AddSubReserv[i][5]=null; AddSubReserv[i][6]=null; AddSubReserv[i][7]=null; AddSubReserv[i][8]=null; AddSubReserv[i][9]=null;AddSubReserv[i][10]=null;
			 }
		 }
		 for (int i = 0; i < MulDivReserv.length; i++) {
			 if(MulDivReserv[i][0]!=null && MulDivReserv[i][8]!=null && checkFIFO==Integer.parseInt(MulDivReserv[i][9])) {
				 tag = "M"+(i+1);value=MulDivReserv[i][7];
				 status+=MulDivReserv[i][11]+" Written at cycle "+(cycles+1)+"\n";
				 MulDivReserv[i][0]=null; MulDivReserv[i][1]=null; MulDivReserv[i][2]=null; MulDivReserv[i][3]=null; MulDivReserv[i][4]=null;
				 MulDivReserv[i][5]=null; MulDivReserv[i][6]=null; MulDivReserv[i][7]=null; MulDivReserv[i][8]=null; MulDivReserv[i][9]=null;MulDivReserv[i][10]=null;
			 }
		 }
		 for (int i = 0; i < LoadBuffer.length; i++) {
			 if(LoadBuffer[i][0]!=null && LoadBuffer[i][4]!=null && checkFIFO==Integer.parseInt(LoadBuffer[i][5])) {
				 tag = "L"+(i+1);value=dm.readData(Integer.parseInt(LoadBuffer[i][1]))+"";status+=LoadBuffer[i][7]+" Written at cycle "+(cycles+1)+"\n";
				 LoadBuffer[i][0]=null; LoadBuffer[i][1]=null; LoadBuffer[i][2]=null; LoadBuffer[i][3]=null; LoadBuffer[i][4]=null;
				 LoadBuffer[i][5]=null; LoadBuffer[i][6]=null;
			 }
		 }
		 for (int i = 0; i < StoreBuffer.length; i++) {
			 if(StoreBuffer[i][0]!=null && StoreBuffer[i][6]!=null && checkFIFO==Integer.parseInt(StoreBuffer[i][7])) {
				 dm.writeData(Integer.parseInt(StoreBuffer[i][1]), Float.parseFloat(StoreBuffer[i][2]));status+=StoreBuffer[i][9]+" Written at cycle "+(cycles+1)+"\n";
				 StoreBuffer[i][0]=null; StoreBuffer[i][1]=null; StoreBuffer[i][2]=null; StoreBuffer[i][3]=null; StoreBuffer[i][4]=null;
				 StoreBuffer[i][5]=null; StoreBuffer[i][6]=null;StoreBuffer[i][7]=null;StoreBuffer[i][8]=null;
			 }
		 }
		 
		 //writing the tag and the value for the instructions that needs it to execute
		 if(!tag.isEmpty()) {
			 for (int i = 0; i < AddSubReserv.length; i++) {
				 if(AddSubReserv[i][0]!=null) {
					 if(AddSubReserv[i][4]!=null && AddSubReserv[i][4].equals(tag)) {
						 AddSubReserv[i][4]=null;AddSubReserv[i][2]=value;
					 }
					 if(AddSubReserv[i][5]!=null && AddSubReserv[i][5].equals(tag)) {
						 AddSubReserv[i][5]=null;AddSubReserv[i][3]=value;
					 }
				 }
			 }
			 for (int i = 0; i < MulDivReserv.length; i++) {
				 if(MulDivReserv[i][0]!=null) {
					 if(MulDivReserv[i][4]!=null && MulDivReserv[i][4].equals(tag)) {
						 MulDivReserv[i][4]=null;MulDivReserv[i][2]=value;
					 }
					 if(MulDivReserv[i][5]!=null && MulDivReserv[i][5].equals(tag)) {
						 MulDivReserv[i][5]=null;MulDivReserv[i][3]=value;
					 }
				 }
			 }
			 for (int i = 0; i < StoreBuffer.length; i++) {
				 if(StoreBuffer[i][0]!=null && StoreBuffer[i][3]!=null && StoreBuffer[i][3].equals(tag)) {
					 StoreBuffer[i][3]=null;StoreBuffer[i][2]=value;
				 }
			 }
			 for (int i = 0; i < CheckIntegerRegister.length; i++) {
				 if(CheckIntegerRegister[i]!=null && CheckIntegerRegister[i].equals(tag)) {
					 IntegerRegister[i]=Integer.parseInt(value);CheckIntegerRegister[i]=null;
				 }
			 }
			 for (int i = 0; i < CheckFloatingPointRegister.length; i++) {
				 if(CheckFloatingPointRegister[i]!=null && CheckFloatingPointRegister[i].equals(tag)) {
					 FloatingPointRegister[i]=Float.parseFloat(value);CheckFloatingPointRegister[i]=null;
				 }
			 }
		 }
		 
	 }
	 
	 
	 public void addToReserveBuffer(int x) {
		//Add/Sub :- //0:Busy,1:op,2:vi,3:vk,4:qi,5:qk,6:Time,7:issued,8:Finished,9:fifo,10:finished recently,11:instruction
		 if(x==1) {
			 for (int i = 0; i < AddSubReserv.length; i++) {
				 if(AddSubReserv[i][0] == null) {
					 AddSubReserv[i][0]="1";AddSubReserv[i][1]=content[0];AddSubReserv[i][7]="1";AddSubReserv[i][9]=""+FIFO++;AddSubReserv[i][11]=im.Instructions[Pc];
					 
					 if(content[0].equals("ADD.D"))AddSubReserv[i][6]=AddDLatency;else if(content[0].equals("DADD"))AddSubReserv[i][6]=DADDLatency; else AddSubReserv[i][6]=SubDLatency;
					 
				
					 
					 if(content[2].charAt(0)=='R') {
						 if(CheckIntegerRegister[Integer.parseInt(content[2].substring(1))]==null || CheckIntegerRegister[Integer.parseInt(content[2].substring(1))].equals("A"+(i+1)))
							 AddSubReserv[i][2]=IntegerRegister[Integer.parseInt(content[2].substring(1))]+"";
						 else AddSubReserv[i][4]=CheckIntegerRegister[Integer.parseInt(content[2].substring(1))];
					 }
					 else {
						 if(CheckFloatingPointRegister[Integer.parseInt(content[2].substring(1))]==null || CheckFloatingPointRegister[Integer.parseInt(content[2].substring(1))].equals("A"+(i+1)))
							 AddSubReserv[i][2]=FloatingPointRegister[Integer.parseInt(content[2].substring(1))]+"";
						 else AddSubReserv[i][4]=CheckFloatingPointRegister[Integer.parseInt(content[2].substring(1))];
					 }
					 
					 if(content[3].charAt(0)=='R') {
						 if(CheckIntegerRegister[Integer.parseInt(content[3].substring(1))]==null || CheckIntegerRegister[Integer.parseInt(content[3].substring(1))].equals("A"+(i+1)))
							 AddSubReserv[i][3]=IntegerRegister[Integer.parseInt(content[3].substring(1))]+"";
						 else AddSubReserv[i][5]=CheckIntegerRegister[Integer.parseInt(content[3].substring(1))];
					 }
					 else {
						 if(CheckFloatingPointRegister[Integer.parseInt(content[3].substring(1))]==null || CheckFloatingPointRegister[Integer.parseInt(content[3].substring(1))].equals("A"+(i+1)))
							 AddSubReserv[i][3]=FloatingPointRegister[Integer.parseInt(content[3].substring(1))]+"";
						 else AddSubReserv[i][5]=CheckFloatingPointRegister[Integer.parseInt(content[3].substring(1))];
					 }
					 
					 if(content[1].charAt(0)=='R')CheckIntegerRegister[Integer.parseInt(content[1].substring(1))]="A"+(i+1);
					 else CheckFloatingPointRegister[Integer.parseInt(content[1].substring(1))]="A"+(i+1);
					 
					 status+=im.Instructions[Pc]+" Issued at cycle "+(cycles+1)+"\n";
					 Pc++;
					 break;
					 }
			 }
		 }
			
		 //Mul/Div :- //0:Busy,1:op,2:vi,3:vk,4:qi,5:qk,6:Time,7:issued,8:Finished,9:fifo,10:finished recently,11:instruction
		 if(x==2) {
			 for (int i = 0; i < MulDivReserv.length; i++) {
				 if(MulDivReserv[i][0] == null) {
					 MulDivReserv[i][0]="1";MulDivReserv[i][1]=content[0];MulDivReserv[i][7]="1";MulDivReserv[i][9]=""+FIFO++;MulDivReserv[i][11]=im.Instructions[Pc];
					 
					 if(content[0].equals("MUL.D"))MulDivReserv[i][6]=MulDLatency; else MulDivReserv[i][6]=DivDLatency;
					 
					 
					 
					 if(content[2].charAt(0)=='R') {
						 if(CheckIntegerRegister[Integer.parseInt(content[2].substring(1))]==null || CheckIntegerRegister[Integer.parseInt(content[2].substring(1))].equals("M"+(i+1)))
							 MulDivReserv[i][2]=IntegerRegister[Integer.parseInt(content[2].substring(1))]+"";
						 else MulDivReserv[i][4]=CheckIntegerRegister[Integer.parseInt(content[2].substring(1))];
					 }
					 else {
						 if(CheckFloatingPointRegister[Integer.parseInt(content[2].substring(1))]==null || CheckFloatingPointRegister[Integer.parseInt(content[2].substring(1))].equals("M"+(i+1)))
							 MulDivReserv[i][2]=FloatingPointRegister[Integer.parseInt(content[2].substring(1))]+"";
						 else MulDivReserv[i][4]=CheckFloatingPointRegister[Integer.parseInt(content[2].substring(1))];
					 }
					 
					 if(content[3].charAt(0)=='R') {
						 if(CheckIntegerRegister[Integer.parseInt(content[3].substring(1))]==null || CheckIntegerRegister[Integer.parseInt(content[3].substring(1))].equals("M"+(i+1)))
							 MulDivReserv[i][3]=IntegerRegister[Integer.parseInt(content[3].substring(1))]+"";
						 else MulDivReserv[i][5]=CheckIntegerRegister[Integer.parseInt(content[3].substring(1))];
					 }
					 else {
						 if(CheckFloatingPointRegister[Integer.parseInt(content[3].substring(1))]==null || CheckFloatingPointRegister[Integer.parseInt(content[3].substring(1))].equals("M"+(i+1)))
							 MulDivReserv[i][3]=FloatingPointRegister[Integer.parseInt(content[3].substring(1))]+"";
						 else MulDivReserv[i][5]=CheckFloatingPointRegister[Integer.parseInt(content[3].substring(1))];
					 }
					 
					 if(content[1].charAt(0)=='R')CheckIntegerRegister[Integer.parseInt(content[1].substring(1))]="M"+(i+1);
					 else CheckFloatingPointRegister[Integer.parseInt(content[1].substring(1))]="M"+(i+1);
					 status+=im.Instructions[Pc]+" Issued at cycle "+(cycles+1)+"\n";
					 Pc++;
					 break;
					 }
			 }
		 }
		 
			//AddI/SubI :- //0:Busy,1:op,2:vi,3:vk,4:qi,5:qk,6:Time,7:issued,8:Finished,9:fifo,10:finished recently,11:instruction
		 if(x==3) {
			 for (int i = 0; i < AddSubReserv.length; i++) {
				 if(AddSubReserv[i][0] == null) {
					 AddSubReserv[i][0]="1";AddSubReserv[i][1]=content[0];AddSubReserv[i][3]=content[3];AddSubReserv[i][7]="1";AddSubReserv[i][9]=""+FIFO++;AddSubReserv[i][11]=im.Instructions[Pc];
					 
					 if(content[0].equals("ADDI"))AddSubReserv[i][6]=ADDILatency; else AddSubReserv[i][6]=SUBILatency;
					 
					 
					 if(content[2].charAt(0)=='R') {
						 if(CheckIntegerRegister[Integer.parseInt(content[2].substring(1))]==null || CheckIntegerRegister[Integer.parseInt(content[2].substring(1))].equals("A"+(i+1)))
							 AddSubReserv[i][2]=IntegerRegister[Integer.parseInt(content[2].substring(1))]+"";
						 else AddSubReserv[i][4]=CheckIntegerRegister[Integer.parseInt(content[2].substring(1))];
					 }
					 else {
						 if(CheckFloatingPointRegister[Integer.parseInt(content[2].substring(1))]==null || CheckFloatingPointRegister[Integer.parseInt(content[2].substring(1))].equals("A"+(i+1)))
							 AddSubReserv[i][2]=FloatingPointRegister[Integer.parseInt(content[2].substring(1))]+"";
						 else AddSubReserv[i][4]=CheckFloatingPointRegister[Integer.parseInt(content[2].substring(1))];
					 }
					 
					 if(content[1].charAt(0)=='R')CheckIntegerRegister[Integer.parseInt(content[1].substring(1))]="A"+(i+1);
					 else CheckFloatingPointRegister[Integer.parseInt(content[1].substring(1))]="A"+(i+1);
					 status+=im.Instructions[Pc]+" Issued at cycle "+(cycles+1)+"\n";
					 Pc++;
					 break;
					 }
			 }
		 }
			//Benz :- //0:Busy,1:op,2:vi,3:vk,4:qi,5:qk,6:Time,7:issued,8:Finished,9:fifo,10:finished recently,11:instruction
		 if(x==4) {
			 for (int i = 0; i < AddSubReserv.length; i++) {
				 if(AddSubReserv[i][0] == null) {
					 AddSubReserv[i][0]="1";AddSubReserv[i][1]=content[0];AddSubReserv[i][3]=content[2];AddSubReserv[i][6]=BenzLatency;AddSubReserv[i][7]="1";
					 AddSubReserv[i][9]=""+FIFO++;branched=true;AddSubReserv[i][11]=im.Instructions[Pc];
					 
					 if(content[1].charAt(0)=='R') {
						 if(CheckIntegerRegister[Integer.parseInt(content[1].substring(1))]==null || CheckIntegerRegister[Integer.parseInt(content[1].substring(1))].equals("A"+(i+1)))
							 AddSubReserv[i][2]=IntegerRegister[Integer.parseInt(content[1].substring(1))]+"";
						 else AddSubReserv[i][4]=CheckIntegerRegister[Integer.parseInt(content[1].substring(1))];
					 }
					 else {
						 if(CheckFloatingPointRegister[Integer.parseInt(content[1].substring(1))]==null || CheckFloatingPointRegister[Integer.parseInt(content[1].substring(1))].equals("A"+(i+1)))
							 AddSubReserv[i][2]=FloatingPointRegister[Integer.parseInt(content[1].substring(1))]+"";
						 else AddSubReserv[i][4]=CheckFloatingPointRegister[Integer.parseInt(content[1].substring(1))];
					 }
					 status+=im.Instructions[Pc]+" Issued at cycle "+(cycles+1)+"\n";
					 Pc++;
					 break;
					 }
			 }
		 }
		 //Load:- //0:Busy,1:address,2:Time,3:issued,4:Finished,5:fifo,6:finished recently,7:instruction
		 if(x==5) {
			 boolean flag = true;
			 for(int i=0;i<StoreBuffer.length;i++) {
				 if(StoreBuffer[i][1]!=null && StoreBuffer[i][1].equals(content[2])) {
					 flag=false;
					 break;}
			 }
			 if (flag) {
			 for (int i = 0; i < LoadBuffer.length; i++) {
				 if(LoadBuffer[i][0] == null) {
					 LoadBuffer[i][0]="1";LoadBuffer[i][1]=content[2];LoadBuffer[i][2]=LDLatency;LoadBuffer[i][3]="1";LoadBuffer[i][5]=""+FIFO++;LoadBuffer[i][7]=im.Instructions[Pc];
					 
					 if(content[1].charAt(0)=='R')CheckIntegerRegister[Integer.parseInt(content[1].substring(1))]="L"+(i+1);
					 else CheckFloatingPointRegister[Integer.parseInt(content[1].substring(1))]="L"+(i+1);
					 status+=im.Instructions[Pc]+" Issued at cycle "+(cycles+1)+"\n";
					 Pc++;
					 break;}
				 }
			 }
			 
		 }
		 //Store:- //0:Busy,1:address,2:v,3:q,4:Time,5:issued,6:Finished,7:fifo,8:finished recently,9:instruction
		 if(x==6) {
			 boolean flag = true;
			 for(int i=0;i<StoreBuffer.length;i++) {
				 if((StoreBuffer[i][1]!=null && StoreBuffer[i][1].equals(content[2])) || (LoadBuffer[i][1]!=null && LoadBuffer[i][1].equals(content[2]))) {
					 flag=false;
					 break;}
			 }
			 if (flag) {
			 for (int i = 0; i < StoreBuffer.length; i++) {
				 if(StoreBuffer[i][0] == null || StoreBuffer[i][0].equals("0")) {
					 StoreBuffer[i][0]="1";StoreBuffer[i][1]=content[2];StoreBuffer[i][4]=SDLatency;StoreBuffer[i][5]="1";StoreBuffer[i][7]=""+FIFO++;StoreBuffer[i][9]=im.Instructions[Pc];
					 
					 if(content[1].charAt(0)=='R') {
						 if(CheckIntegerRegister[Integer.parseInt(content[1].substring(1))]==null)
							 StoreBuffer[i][2]=IntegerRegister[Integer.parseInt(content[1].substring(1))]+"";
						 else StoreBuffer[i][3]=CheckIntegerRegister[Integer.parseInt(content[1].substring(1))];
					 }
					 else {
						 if(CheckFloatingPointRegister[Integer.parseInt(content[1].substring(1))]==null)
							 StoreBuffer[i][2]=FloatingPointRegister[Integer.parseInt(content[1].substring(1))]+"";
						 else StoreBuffer[i][3]=CheckFloatingPointRegister[Integer.parseInt(content[1].substring(1))];
					 }
					 status+=im.Instructions[Pc]+" Issued at cycle "+(cycles+1)+"\n";
					 Pc++;
					 break;}
				 }
		 }


	 }
	 
	 }
	 
	 public boolean isFull(String[][] x) {
		 boolean isFull = true;
		 for (int i = 0; i < x.length; i++) {
			 if (x[i][0] == null) {
				 isFull = false;
				 break;}
			 }
		 return isFull;
	 }
	 
	 public boolean isEmptyTwo(String[][] x) {
		 boolean isEmpty = true;
		 for (int i = 0; i < x.length; i++) {
			 if (x[i][0] !=null) {
				 isEmpty = false;
				 break;}
			 }
		 return isEmpty;
	 }
	 public boolean isEmpty(String[] x) {
		 boolean isEmpty = true;
		 for (int i = 0; i < x.length; i++) {
			 if (x[i] !=null) {
				 isEmpty = false;
				 break;}
			 }
		 return isEmpty;
	 }
	 
	 public String printArray3(String [][]x, int y) {
		 String k="";
		 if(y==1) {
			 for (int i = 0; i < x.length; i++) {
				 String[] firstTwoColumns = Arrays.copyOfRange(x[i], 0, 7); // Extract first two columns
				 k += Arrays.toString(firstTwoColumns)+"\n";
			 }		          
		 }
		 if(y==2) {
			 for (int i = 0; i < x.length; i++) {
				 String[] firstTwoColumns = Arrays.copyOfRange(x[i], 0, 3); // Extract first two columns
	             k += Arrays.toString(firstTwoColumns)+"\n";
	             
			 }	
		 }
		 if(y==3) {
			 for (int i = 0; i < x.length; i++) {
				 String[] firstTwoColumns = Arrays.copyOfRange(x[i], 0, 5); // Extract first two columns
				 k += Arrays.toString(firstTwoColumns)+"\n";
	             
			 }	
		 }
		 return k;
	 }
	 
	 public void loadRegisterR(int address, int value) {
		 IntegerRegister[address]=value;
	 }
	 public void loadRegisterF(int address, float value) {
		 FloatingPointRegister[address]=value;
	 }
	 
	 public void loadMemory(int address, float value) {
		 dm.writeData(address, value);
	 }

	 public void ExecuteProgram() throws Exception {
		 im.loadInstruction(Program_File);
		while(!isEmpty(CheckIntegerRegister)||!isEmpty(CheckFloatingPointRegister)||!isEmptyTwo(AddSubReserv)||!isEmptyTwo(MulDivReserv)||!isEmptyTwo(LoadBuffer)||!isEmptyTwo(StoreBuffer)||Pc<im.size()) {
			 if (im.Instructions[Pc]!=null)content = im.Instructions[Pc].split("\\s|,\\s*|(?<=\\w+:)");
			 if(Pc<im.size()) Issue();
			 Execute();
			 writeBack();
			 cycles++;
			 System.out.println("Pc: " +Pc);
			 System.out.println("Cycle: "+cycles);
			 System.out.println("Integer Registers: "+ Arrays.toString(IntegerRegister));
			 System.out.println("Checking Integer Registers: "+ Arrays.toString(CheckIntegerRegister));
			 System.out.println("Floating Registers: "+ Arrays.toString(FloatingPointRegister));
			 System.out.println("Checking Floating Registers: "+ Arrays.toString(CheckFloatingPointRegister));
			 System.out.println("Data Memory: "+ Arrays.toString(dm.Data));
			 System.out.println("Add/Sub Reserve:-"); printArray3(AddSubReserv,1);System.out.println("Mul/Div Reserve:-");printArray3(MulDivReserv,1);
			 System.out.println("Load Buffer:-");printArray3(LoadBuffer,2);System.out.println("Store Buffer:-");printArray3(StoreBuffer,3);
			 System.out.println("----------------------------------------- \n");
		 }
		 
	 }
	 public String ExecuteProgram(int x) throws Exception {
		 im.loadInstruction(Program_File);
		while(!isEmpty(CheckIntegerRegister)||!isEmpty(CheckFloatingPointRegister)||!isEmptyTwo(AddSubReserv)||!isEmptyTwo(MulDivReserv)||!isEmptyTwo(LoadBuffer)||!isEmptyTwo(StoreBuffer)||Pc<im.size()) {
			 if (im.Instructions[Pc]!=null)content = im.Instructions[Pc].split("\\s|,\\s*|(?<=\\w+:)");
			 if(Pc<im.size()) Issue();
			 Execute();
			 writeBack();
			 cycles++;
			 if(x==cycles) {
				 return (
				 "Pc: " +Pc  +"\n"
				 +"Cycle: "+cycles+"\n"
				 +"Integer Registers: "+ Arrays.toString(IntegerRegister)+"\n"
				 +"Checking Integer Registers: "+ Arrays.toString(CheckIntegerRegister)+"\n"
				 +"Floating Registers: "+ Arrays.toString(FloatingPointRegister)+"\n"
				 +"Checking Floating Registers: "+ Arrays.toString(CheckFloatingPointRegister)+"\n"
				 +"Data Memory: "+ Arrays.toString(dm.Data)+"\n"
				 +"Add/Sub Reserve:- \n" + printArray3(AddSubReserv,1)+ "Mul/Div Reserve:- \n"+printArray3(MulDivReserv,1)
				 +"Load Buffer:- \n"+printArray3(LoadBuffer,2)+"Store Buffer:- \n"+printArray3(StoreBuffer,3)+"\n"
				 +status+"\n"
				 +"----------------------------------------- \n");
				 }
			 
		 }
		return("OK \n"+
				"Pc: " +Pc  +"\n"
				 +"Cycle: "+cycles+"\n"
				 +"Integer Registers: "+ Arrays.toString(IntegerRegister)+"\n"
				 +"Checking Integer Registers: "+ Arrays.toString(CheckIntegerRegister)+"\n"
				 +"Floating Registers: "+ Arrays.toString(FloatingPointRegister)+"\n"
				 +"Checking Floating Registers: "+ Arrays.toString(CheckFloatingPointRegister)+"\n"
				 +"Data Memory: "+ Arrays.toString(dm.Data)+"\n"
				 +"Add/Sub Reserve:- \n" + printArray3(AddSubReserv,1)+ "Mul/Div Reserve:- \n"+printArray3(MulDivReserv,1)
				 +"Load Buffer:- \n"+printArray3(LoadBuffer,2)+"Store Buffer:- \n"+printArray3(StoreBuffer,3)+"\n"
				 +status+"\n"
				 +"----------------------------------------- \n");
		 
	 }
	 //int add, int sub,int mul, int div , int dadd, int subi, int ld, int sd, int reserveA,int reserveM, int loadbuffer, int storebuffer
	 public static void main(String[] args) throws Exception {
		 Processor x = new Processor(2,2,3,4,2,2,2,2,2,2,2,2);
		 x.loadMemory(2, 2);
		 x.loadRegisterR(2, 2);
		 x.loadRegisterF(2, 2);
		 System.out.println(x.ExecuteProgram(50));
	}
}