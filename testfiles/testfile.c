int proc(int y){
    int i;
    i=5;
    while(i<20){
        print("do SOMETHING");
        print(i);
        i=i+1;
    }


    if(i>0){
        print(i);
        print(y);
        print("returns now");
        return i;
    }

    print("unreachable");
}

void main() {
    int i;
    int y2;
    y2=70;
    i=4;
    print(i);
    i=proc(i);
    print(i);
    print(y2);
    print(i);
//    print("Please type an Integer");
//    print(readInt());
}


