import java.util.*;

public class test
{
	public static int q(int y)
	{
		if (y == 0)
		{
			System.out.println("doge");
			return 100;
		}
		else
		{
			int z = y-1;
			return q(z);

//			return 0;
		}
	}

	public static int p(int y)
	{
		if (y != 0)
		{
			int z = y-1;
			return p(z);
		}
		else
		{
			return 100;
		}
	}

	public static void main(String[] argv)
	{
		System.out.println(q(10));
		System.out.println("========================");
		System.out.println(p(10));
	}
}
