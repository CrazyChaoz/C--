//
//int proc(int y,int y2){
//    int i;
//    i=y;
//    print("###");
//    print(y);
//    print("###");
//    print(y2);
//    print("###");
//    while(i<20){
//        print("do SOMETHING");
//        print(i);
//        i=i+1;
//    }
//    if(i>0){
//        print(i);
//        print(y);
//        print("returns now");
//        return i;
//    }
//    print("unreachable");
//}
//
//void main() {
//    int i;
//    int y2;
//    y2=7;
//    i=4;
//    print(i);
//    print(y2);
//    print("asdf");
//    i=proc(i y2);
//    print(i);
//    print(y2);
//    print(i);
//}



int superProcedure(int var, int var2){
    print("-----");
    print(var);
    var=44;
    print("-----");
    var=var+var2;
    return var;
}

int procedure(int var){
    int x;
    x=55;
    print("----##");
    print(superProcedure(x x));
    print("----##");
    return x;
}

void main(){
    int i;
    int j;
    i=3;
    j=10;
    print(i);
    j=procedure(j);
    print(j);
}