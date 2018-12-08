void referencer(ref int i){
    i=i+5;
}

void main(){

    int j;
    j=2;
    referencer(ref j);
    print(j);
}