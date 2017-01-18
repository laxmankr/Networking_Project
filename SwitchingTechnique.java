
import java.io.*;
import java.util.*;
class Source{
    Queue<Packets> q = new LinkedList<Packets>();
    int sid;/*source id*/
    int pgr;/*packet generating rate*/
    int psr;/*packet sending rate*/
    int bandwidth;/*bandwidth of link which is connected with source*/
    public Packets packetGenerating(int id,int time_stamp,int size){
        Packets p=new Packets();
        p.sid=id;
        p.size=size;
        p.time_stamp=time_stamp;
        p.bandwidth=this.bandwidth;
        return p;
    }
    public Packets deque(int sink){
      Packets p= new Packets();
        int delay=0;
            if(!this.q.isEmpty()){
                p = this.q.poll();
                p.sink_time=sink;
               // System.out.println("delay : "+ delay);
            }
        return p;
    }
    
}

class Packets{
    int sid;
    int size;/*Number of bits*/
    int time_stamp;/*Packet generation time instant */
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

class SwitchingTechnique{
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
         System.out.print("\n\n--------------------------------------------------User input-------------------------------------------\n\n");
        System.out.print("Press 1 for circuitSwitching w.r.t number of sources\nPress 2 for packetSwitching with infnite large queue size w.r.t number of sources\nPress 3 for Packetswitching with fixed queue size w.r.t Queue size\n");
        System.out.print("Press 4 for circuitSwitching w.r.t packet sending rate \nPress 5 for packetSwitching with infnite large queue size w.r.t packet sending rate  \nPress 6 for Packetswitching with fixed queue size w.r.t packet sending rate\n");

        System.out.print("\nPlease enter your choice ->> ");
        int choice=in.nextInt();
        System.out.println();
        switch(choice){
            case 1:circuitSwitching();break;
            case 2:packetSwitching();break;
            case 3:packetSwitching_with_fixed_queue_size();break;
            case 4:circuitSwitching_b();break;
            case 5:packetSwitching_b();break;
            case 6:packetSwitching_with_fixed_queue_size_b();break;
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
        System.out.print("Enter packet generating rate for sources : ");
        int packet_generating_rate = in.nextInt();
        System.out.print("Enter packet  sending   rate for sources : ");
        int packet_sending_rate = in.nextInt();
            for(int i=0;i<N;i++){
                source[i] = new Source();
                // System.out.print("Enter packet generating rate for sources "+(i+1)+" : ");
                // source[i].pgr=in.nextInt();
                source[i].pgr = packet_generating_rate;
                // System.out.print("Enter packet  sending   rate for sources "+(i+1)+" : ");
                // source[i].psr=in.nextInt();
                source[i].psr = packet_sending_rate;
            }
        System.out.print("Enter time slice for every source : ");
        int timeSlice=in.nextInt();
         int packet_size=10;
         System.out.print("Enter currenly proccesing source : ");
         int curr_in=in.nextInt()-1;
         System.out.print("Enter Simulation time : ");
         int sim_time=in.nextInt();
 System.out.print("\n\n------------------------------------Output------------------------------------\n\n");
            System.out.println("Number of Sources      Average dealy"); 
            System.out.println("_____________________________________"); 
         for(int n=1;n<=N;n++){
          for(int i=0;i<N;i++){
          source[i].q.clear();
          }
          int curr=curr_in;
            int[] delay= new int[n];
            int[] num_pack= new int[n];
                 for(int i=1;i<=sim_time;i++){
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
                         Packets p = source[curr].deque(i);
                          //System.out.println("->>"+p.sid+" "+p.time_stamp+" "+i);
                            delay[curr] += (i-p.time_stamp); 
                            num_pack[curr]++;
                        }
                    }
                 }
                 // System.out.print("\n\n------------------------------------Output for first "+n+" sources------------------------------------\n\n");
           int total_delay=0;
           int total_packets=0;
           for(int i=0;i<n;i++){
                //System.out.println("Source "+(i+1)+"  total sinked packets :  "+num_pack[i]+"  total delay :   "+delay[i]+"  average delay per packets is    "+Math.round(delay[i]*10.0/num_pack[i])/10.0);
              total_delay+= delay[i];//Math.round(delay[i]*10.0/num_pack[i])/10.0;/*(double) (delay[i]/num_pack[i]);*/  
              total_packets+= num_pack[i];        
           }
          
           System.out.println(n+"                       "+Math.round((double) total_delay*10.0/total_packets)/10.0);
       }

    }
/**Ending of Circuitwitching. */



/**Initiating of Packetswitching with infine large Queue. */
    public static void packetSwitching(){
        Scanner in=new Scanner(System.in);
        System.out.print("Enter no. of Source : ");
        int N=in.nextInt();
        System.out.print("Enter packet size (in bits) : ");
        int packet_size=in.nextInt();
        Source[] source=new Source[N];
        /*User input for each source field as packet generating rate( packets/seccond ), packet sending rate and
        Calculating bandwidth = (packet sending rate)*(Packet size) */
        System.out.print("Enter packet generating rate for sources : ");
        int packet_generating_rate = in.nextInt();
        System.out.print("Enter bandwidth (bits/sec)  for  sources : ");
        int bandwidth_for_all= in.nextInt();
            for(int i=0;i<N;i++){
                source[i]=new Source();
                // System.out.print("Enter packet generating rate for sources "+(i+1)+" : ");
                // source[i].pgr=in.nextInt();
                 source[i].pgr = packet_generating_rate;
                //System.out.print("Enter bandwidth (bits/sec)  for  sources "+(i+1)+" : ");
                source[i].bandwidth=bandwidth_for_all;
                //source[i].bandwidth=source[i].psr*packet_size;
            }


            //creating a Object of Switch class
            Switch obj = new Switch();
             System.out.print("\nEnter packet  sending  rate of switch : ");
             obj.psr=in.nextInt();
             obj.bandwidth=obj.psr*packet_size;
             System.out.print("Enter Simulation time : ");
             int sim_time=in.nextInt();
              System.out.print("\n\n------------------------------------Output------------------------------------\n\n");
            System.out.println("Number of sources     Average delay"); 

              for(int n=1;n<=N;n++){
                obj.q.clear();
                double[] delay = new double[n];
                int[] num_pack = new int[n];
                 for(int i=1;i<=sim_time;i++){
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
                        num_pack[p.sid]++;
                    }
                 }
             int total_delay=0;
             int total_packets=0;
             //System.out.print("\n\n----------------------------------------------Output-----------------------------------------\n\n");
              for(int i=0;i<n;i++){
               // System.out.println("Source "+(i+1)+"  total sinked packets :  "+num_pack[i]+"  total delay :   "+Math.round(delay[i]*10.0)/10.0+"  average delay per packets is    "+Math.round(delay[i]*10.0/num_pack[i])/10.0);
               total_delay+= delay[i];
               total_packets+= num_pack[i];
           }
           System.out.println("   "+n+"                   "+Math.round((double) total_delay*100.0/total_packets)/100.0);
         }

    }
    /*Ending of Packetswitching with infine large Queue. */

  /*---------------------PART-3---Fixed Queue size---------------------. */
    public static void packetSwitching_with_fixed_queue_size(){
         Scanner in=new Scanner(System.in);
        System.out.print("Enter no. of Source : ");
        int n=in.nextInt();
        System.out.print("Enter packet size (in bits) : ");
        int packet_size=in.nextInt();
        // System.out.print("Enter  Queue  size  : ");
        // int Queue_size=in.nextInt();
        Source[] source=new Source[n];
        int total_packet_gen=0;
         System.out.print("Enter bandwidth (bits/sec)  for  sources : ");
        int bandwidth_for_all=in.nextInt();
        System.out.print("Enter packet generating rate for sources : ");
        int packet_generating_rate = in.nextInt();
            for(int i=0;i<n;i++){
                source[i]= new Source();
                // System.out.print("Enter packet generating rate for sources "+(i+1)+" : ");
                // source[i].pgr=in.nextInt();
                 source[i].pgr = packet_generating_rate;
                total_packet_gen+=source[i].pgr;
                // System.out.print("Enter bandwidth (bits/sec)  for  sources "+(i+1)+" : ");
                 source[i].bandwidth= bandwidth_for_all;
            }

            //creating a Object of Switch class
            Switch obj = new Switch();
             System.out.print("\nEnter packet  sending  rate of switch : ");
             obj.psr=in.nextInt();
             obj.bandwidth=obj.psr*packet_size;
             System.out.print("Enter Simulation time : ");
             int sim_time=in.nextInt();
            System.out.print("\n\n----------------------------------------------Output-----------------------------------------\n\n");
            System.out.println("Queue size     Packets loss rate"); 
             for(int Queue_size=1000;Queue_size<=5000;Queue_size+=1000){
              obj.q.clear();
              int packet_Loss = 0;
              double[] delay = new double[n];
              int[] num_pack = new int[n];
                 for(int i=1; i <= sim_time; i++){
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
                    for(int l=0;l<obj.psr;l++){
                      obj.q.poll();
                    }
                    // Collections.sort(list, new Comparator<Packets>(){
                    //       public int compare(Packets p1,Packets p2){
                    //         return p2.bandwidth-p1.bandwidth;
                    //       }
                    //     });
                    // for(int z=0;z<list.size();z++)
                    //     obj.q.add(list.get(z));
                 }
                 //System.out.print("\n\n----------------------------------------------Output-----------------------------------------\n\n");
                  System.out.println("    "+Queue_size+"                   "+Math.round((double) packet_Loss*100.0/sim_time )/100.0);
                  //System.out.println("Total number loss packets : "+packet_Loss);
        }
    }

    /**Initiating of Circuitwitching. */
    public static void circuitSwitching_b(){
          Scanner in=new Scanner(System.in);
        System.out.print("Enter no. of Source : ");
        int n=in.nextInt();
        Source[] source=new Source[n];
        System.out.print("Enter packet generating rate for sources : ");
        int packet_generating_rate = in.nextInt();
        // System.out.print("Enter packet  sending   rate for sources : ");
        // int packet_sending_rate = in.nextInt();
            for(int i=0;i<n;i++){
                source[i] = new Source();
                // System.out.print("Enter packet generating rate for sources "+(i+1)+" : ");
                // source[i].pgr=in.nextInt();
                source[i].pgr = packet_generating_rate;
                // System.out.print("Enter packet  sending   rate for sources "+(i+1)+" : ");
                // source[i].psr=in.nextInt();
                // source[i].psr = packet_sending_rate;
            }
        System.out.print("Enter time slice for every source : ");
        int timeSlice=in.nextInt();
         int packet_size=10;
         System.out.print("Enter currenly proccesing source : ");
         int curr_in=in.nextInt()-1;
         System.out.print("Enter Simulation time : ");
         int sim_time=in.nextInt();
 System.out.print("\n\n------------------------------------Output------------------------------------\n\n");
            System.out.println("Packets sending rate      Average dealy"); 
            System.out.println("_____________________________________"); 
         for(int packet_sending_rate=5;packet_sending_rate<=50;packet_sending_rate+=5){
          for(int i=0;i<n;i++){
                source[i].psr = packet_sending_rate;
                 source[i].q.clear();
            }
          // for(int i=0;i<n;i++){
          // source[i].q.clear();
          // }
          int curr=curr_in;
            int[] delay= new int[n];
            int[] num_pack= new int[n];
            int timer=0;
                 for(int i=1;i<=sim_time;i++){
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
                         Packets p = source[curr].deque(i);
                          //System.out.println("->>"+p.sid+" "+p.time_stamp+" "+i);
                            delay[curr] += (i-p.time_stamp); 
                            num_pack[curr]++;
                        }
                    }
                 }
                 // System.out.print("\n\n------------------------------------Output for first "+n+" sources------------------------------------\n\n");
           int total_delay=0;
           int total_packets=0;
           for(int i=0;i<n;i++){
                //System.out.println("Source "+(i+1)+"  total sinked packets :  "+num_pack[i]+"  total delay :   "+delay[i]+"  average delay per packets is    "+Math.round(delay[i]*10.0/num_pack[i])/10.0);
              total_delay+= delay[i];//Math.round(delay[i]*10.0/num_pack[i])/10.0;/*(double) (delay[i]/num_pack[i]);*/  
              total_packets+= num_pack[i];        
           }
          
           System.out.println("   "+packet_sending_rate+"                       "+Math.round((double) total_delay*10.0/total_packets)/10.0);
       }

    }
/**Ending of Circuitwitching. */



/**Initiating of Packetswitching with infine large Queue. */
    public static void packetSwitching_b(){
        Scanner in=new Scanner(System.in);
        System.out.print("Enter no. of Source : ");
        int n=in.nextInt();
        System.out.print("Enter packet size (in bits) : ");
        int packet_size=in.nextInt();
        Source[] source=new Source[n];
        /*User input for each source field as packet generating rate( packets/seccond ), packet sending rate and
        Calculating bandwidth = (packet sending rate)*(Packet size) */
        System.out.print("Enter packet generating rate for sources : ");
        int packet_generating_rate = in.nextInt();
        System.out.print("Enter bandwidth (bits/sec)  for  sources : ");
        int bandwidth_for_all= in.nextInt();
            for(int i=0;i<n;i++){
                source[i]=new Source();
                // System.out.print("Enter packet generating rate for sources "+(i+1)+" : ");
                // source[i].pgr=in.nextInt();
                 source[i].pgr = packet_generating_rate;
                //System.out.print("Enter bandwidth (bits/sec)  for  sources "+(i+1)+" : ");
                source[i].bandwidth=bandwidth_for_all;
                //source[i].bandwidth=source[i].psr*packet_size;
            }


            //creating a Object of Switch class
            Switch obj = new Switch();
             // System.out.print("\nEnter packet  sending  rate of switch : ");
             // obj.psr=in.nextInt();
             obj.bandwidth=obj.psr*packet_size;
             System.out.print("Enter Simulation time : ");
             int sim_time=in.nextInt();
              System.out.print("\n\n------------------------------------Output------------------------------------\n\n");
            System.out.println("Packet sending rate    Average delay"); 

              for(int packet_sending_rate=10;packet_sending_rate<=100;packet_sending_rate+=10){
                obj.psr=packet_sending_rate;
                obj.q.clear();
                double[] delay = new double[n];
                int[] num_pack = new int[n];
                 for(int i=1;i<=sim_time;i++){
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
                        num_pack[p.sid]++;
                    }
                 }
             int total_delay=0;
             int total_packets=0;
             //System.out.print("\n\n----------------------------------------------Output-----------------------------------------\n\n");
              for(int i=0;i<n;i++){
               // System.out.println("Source "+(i+1)+"  total sinked packets :  "+num_pack[i]+"  total delay :   "+Math.round(delay[i]*10.0)/10.0+"  average delay per packets is    "+Math.round(delay[i]*10.0/num_pack[i])/10.0);
               total_delay+= delay[i];
               total_packets+= num_pack[i];
           }
           System.out.println("   "+packet_sending_rate+"                   "+Math.round((double) total_delay*100.0/total_packets)/100.0);
         }

    }
    /*Ending of Packetswitching with infine large Queue. */

  /*---------------------PART-3---Fixed Queue size---------------------. */
    public static void packetSwitching_with_fixed_queue_size_b(){
         Scanner in=new Scanner(System.in);
        System.out.print("Enter no. of Source : ");
        int n=in.nextInt();
        System.out.print("Enter packet size (in bits) : ");
        int packet_size=in.nextInt();
        System.out.print("Enter  Queue  size  : ");
        int Queue_size=in.nextInt();
        Source[] source=new Source[n];
        int total_packet_gen=0;
        System.out.print("Enter bandwidth (bits/sec)  for  sources : ");
        int bandwidth_for_all=in.nextInt();
        System.out.print("Enter packet generating rate for sources : ");
        int packet_generating_rate = in.nextInt();
            for(int i=0;i<n;i++){
                source[i]= new Source();
                // System.out.print("Enter packet generating rate for sources "+(i+1)+" : ");
                // source[i].pgr=in.nextInt();
                 source[i].pgr = packet_generating_rate;
                total_packet_gen+=source[i].pgr;
                // System.out.print("Enter bandwidth (bits/sec)  for  sources "+(i+1)+" : ");
                 source[i].bandwidth= bandwidth_for_all;
            }

            //creating a Object of Switch class
            Switch obj = new Switch();
             // System.out.print("\nEnter packet  sending  rate of switch : ");
             // obj.psr=in.nextInt();
             obj.bandwidth=obj.psr*packet_size;
             System.out.print("Enter Simulation time : ");
             int sim_time=in.nextInt();
            System.out.print("\n\n----------------------------------------------Output-----------------------------------------\n\n");
            System.out.println("packet sending rate     Packets loss rate"); 
             for(int packet_sending_rate=10;packet_sending_rate<=100;packet_sending_rate+=10){
              obj.q.clear();
              obj.psr=packet_sending_rate;
              int packet_Loss = 0;
              double[] delay = new double[n];
              int[] num_pack = new int[n];
                 for(int i=1; i <= sim_time; i++){
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
                    for(int l=0;l<obj.psr;l++){
                      obj.q.poll();
                    }
                    // Collections.sort(list, new Comparator<Packets>(){
                    //       public int compare(Packets p1,Packets p2){
                    //         return p2.bandwidth-p1.bandwidth;
                    //       }
                    //     });
                    // for(int z=0;z<list.size();z++)
                    //     obj.q.add(list.get(z));
                 }
                 //System.out.print("\n\n----------------------------------------------Output-----------------------------------------\n\n");
                  System.out.println("    "+packet_sending_rate+"                   "+Math.round((double) packet_Loss*100.0/sim_time )/100.0);
                  //System.out.println("Total number loss packets : "+packet_Loss);
        }
    }
    
}

