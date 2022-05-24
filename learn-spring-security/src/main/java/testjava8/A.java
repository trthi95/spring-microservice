package testjava8;

public class A {
	private FunctInterface functInterface;

	public A(FunctInterface functInterface) {
		this.functInterface = functInterface;
	}

	@FunctionalInterface
	public interface FunctInterface {
		int sum(int a, int b);
	}

	static class Math {
		public static int sum(int a, int b) {
			return a + b;
		}

		public static int minus(int a, int b) {
			return a - b;
		}
	}

	public static void main(String[] args) {
		FunctInterface functInterface = (Math::sum);
		int result = functInterface.sum(1, 2);
		System.out.println(result);
		
		FunctInterface aa = new FunctInterface() {
			
			@Override
			public int sum(int a, int b) {
				// TODO Auto-generated method stub
				return 0;
			}
		};
		
		FunctInterface functInterface1 = (a, b) -> a + b;
		int result1 = functInterface1.sum(3, 4);
		System.out.println(result1);

	}

}
