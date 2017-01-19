//jskdkdf
import java.io.*;
import java.util.*;
class SwithingTechnique{
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
         System.out.print("\n\n--------------------------------------------------User input-------------------------------------------\n\n");
        System.out.print("Press 1 for circuitSwitching\nPress 2 for packetSwitching with infnite large queue size\nPress 3 for Packetswitching with fixed queue size\n");
        System.out.print("\nPlease enter your choice ->> ");
        int choice=in.nextInt();
        System.out.println();
        switch(choice){
            case 1:circuitSwitching();break;
            case 2:packetSwitching();break;
            case 3:packetSwitching_with_fixed_queue_size();break;
            default:System.out.println("Sorry!! Wrong input"); 
        }
 System.out.print("\n-----------------------------------------------End------------------------------------------------\n\n");
    }


/**Initiating of Circuitwitching. */
    public static void circuitSwitching(){
          Scanner in=new Scanner(System.in);
        System.out.print("Enter no. of Source : ");
        int N=in.nextInt();
        Source[] source=new Source[N];

            for(int i=0;i<N;i++){
                source[i] = new Source();
                System.out.print("Enter packet generating rate for source "+(i+1)+" : ");
                source[i].pgr=in.nextInt();
                System.out.print("Enter packet  sending   rate for source "+(i+1)+" : ");
                source[i].psr=in.nextInt();
            }
        System.out.print("Enter time slice for every source : ");
        int timeSlice=in.nextInt();
         int packet_size=10;
         System.out.print("Enter currently proccesing source : ");
         int curr_in=in.nextInt()-1;
         System.out.print("Enter Simulation time : ");
         int simulation_time=in.nextInt();


         for(int n=1;n<=N;n++){
          int curr=curr_in;
            int[] delay= new int[n];
            int[] num_of_pack= new int[n];
                 for(int i=1;i<=simulation_time;i++){
                    for(int j=0;j<n;j++){
                        for(int k=0;k<source[j].pgr;k++){
                            source[j].q.add(source[j].packetGenerating(j,i,packet_size));
                        }
                    }
                    if((i-1)%timeSlice==0 && i!=1){
                        curr++;
                        if(curr==n)curr=0;
                    }
                
                    for(int k=0;k<source[curr].psr;k++){
                        if(!source[curr].q.isEmpty()){
                            delay[curr] = delay[curr] + source[curr].deque(i);
                            num_of_pack[curr]++;
                        }
                    }
                 }
                  System.out.print("\n\n------------------------------------Output for first "+n+" sources------------------------------------\n\n");
           for(int i=0;i<n;i++){
                System.out.println("Source "+(i+1)+"  total sinked packets :  "+num_of_pack[i]+"  total delay :   "+delay[i]+"  average delay per packets is    "+Math.round(delay[i]*10.0/num_of_pack[i])/10.0);
           }
           System.out.println();
       }

    }
/**Ending of Circuitwitching. */



/**Initiating of Packetswitching with infnite large Queue. */
    public static void packetSwitching(){
        Scanner in=new Scanner(System.in);
        System.out.print("Enter no. of Source : ");
        int n=in.nextInt();
        System.out.print("Enter packet size (in bits) : ");
        int packet_size=in.nextInt();
        Source[] source=new Source[n];
        /*User input for each source field as packet generating rate( packets/seccond ), packet sending rate and
        Calculating bandwidth = (packet sending rate)*(Packet size) */
            for(int i=0;i<n;i++){
                source[i]=new Source();
                System.out.print("Enter packet generating rate for sources "+(i+1)+" : ");
                source[i].pgr=in.nextInt();
                System.out.print("Enter bandwidth (bits/sec)  for  sources "+(i+1)+" : ");
                source[i].bandwidth=in.nextInt();
                //source[i].bandwidth=source[i].psr*packet_size;
            }


            //creating a Object of Switch class
            Switch obj = new Switch();
             System.out.print("\nEnter packet  sending  rate of switch to sink : ");
             obj.psr=in.nextInt();
             obj.bandwidth=obj.psr*packet_size;
             System.out.print("Enter Simulation time : ");
             int simulation_time=in.nextInt();
              double[] delay = new double[n];
              int[] num_of_pack = new int[n];


             for(int i=1;i<=simulation_time;i++){
                ArrayList<Packets> list = new ArrayList<Packets>();
                for(int j=0;j<n;j++){
                    //ArrayList<Packets> list1 = new ArrayList<Packets>();
                    // for(int k=0;k<source[j].pgr;k++){
                    //     obj.q.add(source[j].packetGenerating(j,i,packet_size));
                    // }
                  for(int k=0;k<source[j].pgr;k++){
                        list.add(source[j].packetGenerating(j,i,packet_size)); 
                    }
                    
                }
                Collections.sort(list, new Comparator<Packets>(){
                      public int compare(Packets p1,Packets p2){
                        return p2.bandwidth-p1.bandwidth;
                      }
                    });
                for(int z=0;z<list.size();z++)
                    obj.q.add(list.get(z));
                    //System.out.print(list.get(z).bandwidth+"  ");
                /*In this for loop packets sending from switch to sink in 1 second*/
                for(int j=0;j<obj.psr;j++){
                    Packets p=obj.deque(i);
                    delay[p.sid]+=(p.sink_time-p.time_stamp)+(double) packet_size/source[p.sid].bandwidth;
                    num_of_pack[p.sid]++;
                }
             }
             System.out.print("\n\n----------------------------------------------Output-----------------------------------------\n\n");
              for(int i=0;i<n;i++){
                System.out.println("Source "+(i+1)+"  total sinked packets :  "+num_of_pack[i]+"  total delay :   "+Math.round(delay[i]*10.0)/10.0+"  average delay per packets is    "+Math.round(delay[i]*10.0/num_of_pack[i])/10.0);
           }
           System.out.println();

    }
    /*Ending of Packetswitching with infine large Queue. */

  /*---------------------PART-3---Fixed Queue size---------------------. */
    public static void packetSwitching_with_fixed_queue_size(){
         Scanner in=new Scanner(System.in);
        System.out.print("Enter no. of Source : ");
        int n=in.nextInt();
        System.out.print("Enter packet size (in bits) : ");
        int packet_size=in.nextInt();
        System.out.print("Enter  Queue  size  : ");
        int Queue_size=in.nextInt();
        Source[] source=new Source[n];
        int total_packet_gen=0;
            for(int i=0;i<n;i++){
                source[i]=new Source();
                System.out.print("Enter packet generating rate for sources "+(i+1)+" : ");
                source[i].pgr=in.nextInt();
                total_packet_gen+=source[i].pgr;
                System.out.print("Enter bandwidth (bits/sec)  for  sources "+(i+1)+" : ");
                source[i].bandwidth=in.nextInt();
            }

            //creating a Object of Switch class
            Switch obj = new Switch();
             System.out.print("\nEnter packet  sending  rate of switch : ");
             obj.psr=in.nextInt();
             obj.bandwidth=obj.psr*packet_size;
             System.out.print("Enter Simulation time : ");
             int simulation_time=in.nextInt();
             int packet_Loss = 0;
              double[] delay = new double[n];
              int[] num_of_pack = new int[n];


             for(int i=1; i <= simulation_time; i++){
                //ArrayList<Packets> list = new ArrayList<Packets>();
                for(int j=0;j<n;j++){
                  for(int k=0;k<source[j].pgr;k++){
                      if(obj.q.size()<Queue_size){
                          obj.q.add(source[j].packetGenerating(j,i,packet_size));
                        }
                        else{
                          packet_Loss++;
                        }
                    }
                }
                // Collections.sort(list, new Comparator<Packets>(){
                //       public int compare(Packets p1,Packets p2){
                //         return p2.bandwidth-p1.bandwidth;
                //       }
                //     });
                // for(int z=0;z<list.size();z++)
                //     obj.q.add(list.get(z));
             }
             System.out.print("\n\n----------------------------------------------Output-----------------------------------------\n\n");
              System.out.println("Packets loss rate : "+(double) packet_Loss/simulation_time );
              System.out.println("Total number loss packets : "+packet_Loss);
    }
    
}

class Source{
    Queue<Packets> q = new LinkedList<Packets>();
    int sid;
    int pgr;
    int psr;
    int bandwidth;
    public Packets packetGenerating(int id,int time_stamp,int size){
        Packets p=new Packets();
        p.sid=id;
        p.size=size;
        p.time_stamp=time_stamp;
        p.bandwidth=this.bandwidth;
        return p;
    }
    public int deque(int sink){
        int delay=0;
            if(!q.isEmpty()){
                Packets p = this.q.poll();
                p.sink_time=sink;
                delay = (p.sink_time - p.time_stamp);
               // System.out.println("delay : "+ delay);
            }
        return delay;
    }
    
}

class Packets{
    int sid;
    int size;/*Number of bits*/
    int time_stamp;/*Packet generating time instant */
    int sink_time;/*sinked timing*/
    int bandwidth;/*Bandwidth unit bits/second */

    
}
class Switch{
    Queue<Packets> q = new LinkedList<Packets>();
    int psr;/*Packet sending rate for Switch to Sink*/
    int bandwidth;/*bit per second*/

    public Packets deque(int sink){
        Packets p = new Packets();
         int delay=0;
            if(!q.isEmpty()){
                p=q.poll();
                p.sink_time=sink;
                delay=(p.sink_time-p.time_stamp);
            }
        return p;
    }
  }
