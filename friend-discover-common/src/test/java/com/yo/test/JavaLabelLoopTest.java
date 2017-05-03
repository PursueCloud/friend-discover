package com.yo.test;

/**
 * Created by Yo on 2017/3/29.
 */
public class JavaLabelLoopTest {
    public static void main(String[] args) {
        outer :
        for(int i=0; i<10; i++) {
            inner:
            for(int j=0; j<3; j++) {
                if( j == 2 ) {
                    continue inner;
                }
                if( j == 1 ) {
                    continue outer;
                }
            }
        }
    }
}
