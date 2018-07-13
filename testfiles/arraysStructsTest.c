
struct simpleStruct{
    char[3] einCharArray;
    int einInt;
}


struct advancedStruct{
   char[4][4][4] abc;
   simpleStruct[2] theSimpleStruct;
}

void main(){
    advancedStruct adv1;
    int i;

    adv1.abc[0][0][0]='a';
    adv1.abc[0][0][1]='b';
    adv1.abc[0][0][2]='c';
    adv1.abc[0][0][3]='d';
    adv1.abc[0][1][0]='e';
    adv1.abc[0][1][1]='f';
    adv1.abc[0][1][2]='g';
    adv1.abc[0][1][3]='h';
    adv1.abc[1][0][0]='i';
    adv1.abc[1][0][1]='j';
    adv1.abc[1][0][2]='k';
    adv1.abc[1][0][3]='l';

    adv1.theSimpleStruct[0].einCharArray[0]='z';

    i=2;


    print(adv1.abc[0][1][1]);
    print(adv1.theSimpleStruct[0].einCharArray[0]);

}



