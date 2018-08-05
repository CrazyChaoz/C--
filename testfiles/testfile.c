int proc(); forward;
void main() {
    int i;
    i=proc();
    print("Please type an Integer");
    i=readInt();
    print(i);
}

int proc() {

    if(2>0){
        print("returns now");
        print("returns now");
        print("returns now");
        return 3;
    }

    print("unreachable");
}

