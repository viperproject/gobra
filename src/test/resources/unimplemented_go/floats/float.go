package main

func test() []float64 {
	arr := [...]float64{
		0.,
		72.40,
		072.40,
		2.71828,
		1.e+0,
		6.67428e-11,
		1e6,
		.25,
		.12345e+5,
		1_5.,
		0.15e+0_2,
		/*
			hex not supported
			0x1p-2  ,
			0x2.p10   ,
			0x1.Fp+0  ,
			0X.8p-0  ,
			0X_1FFFP-16 ,
			0x15e-2,
		*/
	}
	return arr[1:1]
}
