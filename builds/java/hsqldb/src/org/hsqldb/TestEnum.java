package org.hsqldb;

public enum TestEnum {
   X, Y , Z;
   public TestEnum f() {
	   return X;
   }
   static {
	   System.err.println("Hello Enum!");
   }
}
