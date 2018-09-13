int proc(); forward;
void main() {
    int i;
    i=proc();
    print("Please type an Integer");
    i=readInt();
    print(i);
}


int proc(){
    int i;
    i=0;
    while(i<20&&10>5){
        print("do SOMETHING");
        print(i);
        i=i+1;
    }


    if(2>0 && 3>2){
            print("1");
            print("2");
            print("returns now");
            return i;
        }

        print("unreachable");
    }
