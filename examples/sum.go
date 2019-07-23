package _; // Should fail.

requires 0 <= n;
ensures 2 * r == n * (n+1);
func sum(n int) (r int) {
  invariant 2 * r == (i-1) * i;
  invariant i <= n + 1;
  for i := 0; i <= n; i++ {
    r += i;
  };
};

requires 0 <= n;
ensures 6 * r == n * (n+1) * (2*n+1);
func sum_sq (n int) (r int) {
  invariant 6 * r == i * (i-1) * (2*i-1);
  invariant i <= n + 1;
  for i := 0; i <= n; i++ { r += i * i; };
};

requires 0 <= n;
ensures 4 * r == n * n * (n+1) * (n+1);
func sum_cb (n int) (r int) {
  invariant 4 * r == (i-1) * (i-1) * i * i;
  invariant i <= n + 1;
  for i := 0; i <= n; i++ { r += i * i * i; };
};

requires 0 <= n;
ensures r == n * n * n * n * n * n * n * n;
func power_8_linearly (n int) (r int) {
  p1, p2, p3, p4, p5, p6, p7, p8 := 0, 0, 0, 0, 0, 0, 0, 0;
  invariant p1 == i;
  invariant p2 == i * p1;
  invariant p3 == i * p2;
  invariant p4 == i * p3;
  invariant p5 == i * p4;
  invariant p6 == i * p5;
  invariant p7 == i * p6;
  invariant p8 == i * p7;
  invariant i <= n;
  for i := 0; i < n; i++ {
    p8 += 8 * p7 + 28 * p6 + 56 * p5 + 70 * p4 + 56 * p3 + 28 * p2 + 8 * p1 + 1;
    p7 += 7 * p6 + 21 * p5 + 35 * p4 + 35 * p3 + 21 * p2 + 7 * p1 + 1;
    p6 += 6 * p5 + 15 * p4 + 20 * p3 + 15 * p2 + 6 * p1 + 1;
    p5 += 5 * p4 + 10 * p3 + 10 * p2 + 5 * p1 + 1;
    p4 += 4 * p3 + 6 * p2 + 4 * p1 + 1;
    p3 += 3 * p2 + 3 * p1 + 1;
    p2 += 2 * p1 + 1;
    p1++;
  };
  r = p8;
};

requires 0 <= n;
ensures r == n * n * n * n * n * n * n * n;
func power_8_linearly_2 (n int) (r int) {
  o0, o1, o2, o3, o4, o5, o6, o7, o8 := 0, 1, 256, 6561, 65536, 390625, 1679616, 5764801, 16777216;
  invariant o0 == i * i * i * i * i * i * i * i;
  invariant o1 == (i - 1) * (i - 1) * (i - 1) * (i - 1) * (i - 1) * (i - 1) * (i - 1) * (i - 1);
  invariant o2 == (i - 2) * (i - 2) * (i - 2) * (i - 2) * (i - 2) * (i - 2) * (i - 2) * (i - 2);
  invariant o3 == (i - 3) * (i - 3) * (i - 3) * (i - 3) * (i - 3) * (i - 3) * (i - 3) * (i - 3);
  invariant o4 == (i - 4) * (i - 4) * (i - 4) * (i - 4) * (i - 4) * (i - 4) * (i - 4) * (i - 4);
  invariant o5 == (i - 5) * (i - 5) * (i - 5) * (i - 5) * (i - 5) * (i - 5) * (i - 5) * (i - 5);
  invariant o6 == (i - 6) * (i - 6) * (i - 6) * (i - 6) * (i - 6) * (i - 6) * (i - 6) * (i - 6);
  invariant o7 == (i - 7) * (i - 7) * (i - 7) * (i - 7) * (i - 7) * (i - 7) * (i - 7) * (i - 7);
  invariant o8 == (i - 8) * (i - 8) * (i - 8) * (i - 8) * (i - 8) * (i - 8) * (i - 8) * (i - 8);
  invariant i <= n;
  for i := 0; i < n; i++ {
    tmp := 9 * (o0 - o7) + 36 * (o6 - o1) + 84 * (o2 - o5) + 126 * (o4 - o3) + o8;
    o8 = o7;
    o7 = o6;
    o6 = o5;
    o5 = o4;
    o4 = o3;
    o3 = o2;
    o2 = o1;
    o1 = o0;
    o0 = tmp;
  };
  r = o0;
};

requires 0 <= n;
ensures r == n * n * n * n * n * n * n * n;
func power_8_no_multiplication (n int) (r int) {
  o0, o1, o2, o3, o4, o5, o6, o7, o8 := 0, 1, 256, 6561, 65536, 390625, 1679616, 5764801, 16777216;
  invariant o0 == i * i * i * i * i * i * i * i;
  invariant o1 == (i - 1) * (i - 1) * (i - 1) * (i - 1) * (i - 1) * (i - 1) * (i - 1) * (i - 1);
  invariant o2 == (i - 2) * (i - 2) * (i - 2) * (i - 2) * (i - 2) * (i - 2) * (i - 2) * (i - 2);
  invariant o3 == (i - 3) * (i - 3) * (i - 3) * (i - 3) * (i - 3) * (i - 3) * (i - 3) * (i - 3);
  invariant o4 == (i - 4) * (i - 4) * (i - 4) * (i - 4) * (i - 4) * (i - 4) * (i - 4) * (i - 4);
  invariant o5 == (i - 5) * (i - 5) * (i - 5) * (i - 5) * (i - 5) * (i - 5) * (i - 5) * (i - 5);
  invariant o6 == (i - 6) * (i - 6) * (i - 6) * (i - 6) * (i - 6) * (i - 6) * (i - 6) * (i - 6);
  invariant o7 == (i - 7) * (i - 7) * (i - 7) * (i - 7) * (i - 7) * (i - 7) * (i - 7) * (i - 7);
  invariant o8 == (i - 8) * (i - 8) * (i - 8) * (i - 8) * (i - 8) * (i - 8) * (i - 8) * (i - 8);
  invariant i <= n;
  for i := 0; i < n; i++ {
    d07 := o0 - o7;
    d43 := o4 - o3;
    d61 := o6 - o1;
    d25 := o2 - o5;
    tmp := d25 + d43;
    tmp := tmp + tmp;
    tmp := tmp + tmp + d61 + d43;
    tmp := tmp + tmp + d61 - d25;
    tmp := tmp + tmp + d43 + d07;
    tmp := tmp + tmp + d07;
    tmp := tmp + tmp + tmp + o8;
    o8 = o7;
    o7 = o6;
    o6 = o5;
    o5 = o4;
    o4 = o3;
    o3 = o2;
    o2 = o1;
    o1 = o0;
    o0 = tmp;
  };
  r = o0;
};


