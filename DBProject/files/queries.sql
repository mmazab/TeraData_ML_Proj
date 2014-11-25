select
	s_acctbal,
	s_name,
	n_name,
	p_partkey,
	p_mfgr,
	s_address,
	s_phone,
	s_comment
from
	part,
	supplier,
	partsupp,
	nation,
	region
where
	p_partkey = ps_partkey
	and s_suppkey = ps_suppkey
	and p_size = 12
	and p_type like '%NICKEL'
	and s_nationkey = n_nationkey
	and n_regionkey = r_regionkey
	and r_name = 'AFRICA'
	and ps_supplycost = (
		select
			min(ps_supplycost)
		from
			partsupp,
			supplier,
			nation,
			region
		where
			p_partkey = ps_partkey
			and s_suppkey = ps_suppkey
			and s_nationkey = n_nationkey
			and n_regionkey = r_regionkey
			and r_name = 'AFRICA'
	)
order by
	s_acctbal desc,
	n_name,
	s_name,
	p_partkey;







select
	o_orderstatus,
	sum(l_extendedprice * cast ((1 - l_discount) as decimal(18,2)) ) as revenue
from
	customer,
	orders,
	lineitem,
	supplier
where
	c_custkey = o_custkey
	and l_orderkey = o_orderkey
	and l_suppkey = s_suppkey
group by
	o_orderstatus
order by
	revenue desc;


select
	s_acctbal,
	s_name,
	n_name,
	p_partkey,
	p_mfgr,
	s_address,
	s_phone,
	s_comment
from
	part,
	supplier,
	partsupp,
	nation,
	region
where
	p_partkey = ps_partkey
	and s_suppkey = ps_suppkey
	and s_nationkey = n_nationkey
	and n_regionkey = r_regionkey
	and r_name = 'EUROPE'
	and ps_supplycost = (
		select
			max(ps_supplycost)
		from
			partsupp,
			supplier,
			nation,
			region
		where
			p_partkey = ps_partkey
			and s_suppkey = ps_suppkey
			and s_nationkey = n_nationkey
			and n_regionkey = r_regionkey
			and r_name = 'EUROPE'
	)
order by
	s_acctbal desc,
	n_name,
	s_name;

select
	p_partkey,
	p_mfgr,
	s_address,
	s_phone,
	s_comment
from
	part,
	supplier,
	partsupp,
	nation,
	region
where
	p_partkey = ps_partkey
	and s_suppkey = ps_suppkey
	and p_size = 35
	and s_nationkey = n_nationkey
	and n_regionkey = r_regionkey
	and r_name = 'AFRICA'
	and ps_supplycost = (
		select
			min(ps_supplycost)
		from
			partsupp,
			supplier,
			nation,
			region
		where
			p_partkey = ps_partkey
			and s_suppkey = ps_suppkey
			and s_nationkey = n_nationkey
			and n_regionkey = r_regionkey
			and r_name = 'AFRICA'
	)
order by
	p_partkey;

select
	p_partkey,
	p_mfgr,
	s_address,
	s_phone,
	s_comment
from
	part,
	supplier,
	partsupp,
	nation,
	region
where
	p_partkey = ps_partkey
	and s_suppkey = ps_suppkey
	and s_nationkey = n_nationkey
	and n_regionkey = r_regionkey
	and r_name = 'AFRICA'
	and ps_supplycost = (
		select
			min(ps_supplycost)
		from
			partsupp,
			supplier,
			nation,
			region
		where
			p_partkey = ps_partkey
			and s_suppkey = ps_suppkey
			and s_nationkey = n_nationkey
			and n_regionkey = r_regionkey
			and r_name = 'AFRICA'
	)
order by
	p_mfgr;

select
	s_acctbal,
	s_name,
	n_name,
	p_partkey,
	p_mfgr,
	s_address,
	s_phone,
	s_comment
from
	part,
	supplier,
	partsupp,
	nation,
	region
where
	p_partkey = ps_partkey
	and s_suppkey = ps_suppkey
	and s_nationkey = n_nationkey
	and n_regionkey = r_regionkey
	and r_name = 'AFRICA'
	and ps_supplycost = (
		select
			max(ps_supplycost)
		from
			partsupp,
			supplier,
			nation,
			region
		where
			p_partkey = ps_partkey
			and s_suppkey = ps_suppkey
			and s_nationkey = n_nationkey
			and n_regionkey = r_regionkey
			and r_name = 'AFRICA'
	)
order by
	s_acctbal desc,
	n_name;

select
	l_orderkey,
	sum(l_extendedprice * (1 - l_discount)) as revenue,
	o_orderdate,
	o_shippriority
from
	customer,
	orders,
	lineitem
where
	c_mktsegment = 'FURNITURE'
	and c_custkey = o_custkey
	and l_orderkey = o_orderkey
	and o_orderdate < date '1995-03-04'
	and l_shipdate > date '1995-03-04'
group by
	l_orderkey,
	o_orderdate,
	o_shippriority
order by
	revenue desc,
	o_orderdate;


	
select
	l_orderkey,
	sum(l_extendedprice * (1 - l_discount)) as revenue,
	o_orderdate
from
	customer,
	orders,
	lineitem
where
	c_mktsegment = 'AUTOMOBILE'
	and c_custkey = o_custkey
	and l_orderkey = o_orderkey
group by
	l_orderkey,
	o_orderdate
order by
	revenue desc,
	o_orderdate;

select
	l_orderkey,
	avg(l_extendedprice * (1 - l_discount)) as avgrevenue,
	o_orderdate,
	o_shippriority
from
	customer,
	orders,
	lineitem
where
	c_custkey = o_custkey
	and l_orderkey = o_orderkey
	and o_orderdate > date '1992-03-04'
group by
	l_orderkey,
	o_orderdate,
	o_shippriority
order by
	avgrevenue,
	o_orderdate;

select
	sum(l_extendedprice * (1 - l_discount)) as revenue,
	o_shippriority
from
	customer,
	orders,
	lineitem
where
	c_mktsegment = 'MACHINERY'
	and c_custkey = o_custkey
	and l_orderkey = o_orderkey
	and l_shipdate > date '1994-06-07'
group by
	o_shippriority
order by
	revenue desc;

select
	o_orderpriority,
	count(*) as order_count
from
	orders
where
	o_orderdate >= date '1993-06-01'
	and o_orderdate < date '1993-06-01' + interval '3' month
	and exists (
		select
			*
		from
			lineitem
		where
			l_orderkey = o_orderkey
			and l_commitdate < l_receiptdate
	)
group by
	o_orderpriority
order by
	o_orderpriority;

select
	o_orderpriority,
	count(*) as order_count
from
	orders
where
	o_orderdate >= date '1993-06-01'
	and o_orderdate < date '1993-06-01' + interval '3' month
group by
	o_orderpriority
order by
	o_orderpriority;

select
	o_orderpriority,
	count(*) as order_count
from
	orders
where
	o_orderdate >= date '1995-04-01'
	and o_orderdate < date '1995-04-01' + interval '8' month
	and o_orderstatus = 'O'
	and exists (
		select
			*
		from
			lineitem
		where
			l_orderkey = o_orderkey
			and l_commitdate < l_shipdate
	)
group by
	o_orderpriority
order by
	o_orderpriority;






select
	o_orderpriority,
	avg(o_totalprice) as avg_orderprice
from
	orders
where
	o_orderdate >= date '1992-06-01'
	and o_orderstatus = 'F'
	and not exists (
		select
			*
		from
			lineitem
		where
			l_orderkey = o_orderkey
			and l_commitdate > l_receiptdate
	)
group by
	o_orderpriority
order by
	o_orderpriority;

select
	n_name,
	sum(l_extendedprice * (1 - l_discount)) as revenue
from
	customer,
	orders,
	lineitem,
	supplier,
	nation,
	region
where
	c_custkey = o_custkey
	and l_orderkey = o_orderkey
	and l_suppkey = s_suppkey
	and c_nationkey = s_nationkey
	and s_nationkey = n_nationkey
	and n_regionkey = r_regionkey
	and r_name = 'AMERICA'
	and o_orderdate >= date '1993-01-01'
	and o_orderdate < date '1993-01-01' + interval '1' year
group by
	n_name
order by
	revenue desc;

select
	n_name,
	sum(l_extendedprice * (1 - l_discount)) as revenue
from
	customer,
	orders,
	lineitem,
	supplier,
	nation,
	region
where
	c_custkey = o_custkey
	and l_orderkey = o_orderkey
	and l_suppkey = s_suppkey
	and s_nationkey = n_nationkey
	and n_regionkey = r_regionkey
	and r_name = 'ASIA'
group by
	n_name
order by
	revenue desc;

select
	n_name,
	sum(l_extendedprice * (1 - l_discount)) as revenue
from
	customer,
	orders,
	lineitem,
	supplier,
	nation,
	region
where
	c_custkey = o_custkey
	and l_orderkey = o_orderkey
	and l_suppkey = s_suppkey
	and s_nationkey = n_nationkey
	and n_regionkey = r_regionkey
	and r_name = 'EUROPE'
group by
	n_name
order by
	revenue desc;

select
	n_name,
	avg(l_extendedprice * (1 - l_discount)) as avgrevenue
from
	customer,
	orders,
	lineitem,
	supplier,
	nation,
	region
where
	c_custkey = o_custkey
	and l_orderkey = o_orderkey
	and l_suppkey = s_suppkey
	and s_nationkey = n_nationkey
	and n_regionkey = r_regionkey
	and r_name = 'ASIA'
	and o_orderstatus = 'F'
group by
	n_name
order by
	avgrevenue;

select
	o_orderstatus,
	sum(l_extendedprice * cast ((1 - l_discount) as decimal(18,2)) ) as revenue
from
	customer,
	orders,
	lineitem,
	supplier
where
	c_custkey = o_custkey
	and l_orderkey = o_orderkey
	and l_suppkey = s_suppkey
group by
	o_orderstatus
order by
	revenue desc;





select
	sum(l_extendedprice * l_discount) as revenue
from
	lineitem
where
	l_shipdate >= date '1993-01-01'
	and l_shipdate < date '1993-01-01' + interval '1' year
	and l_discount between 0.05 - 0.01 and 0.05 + 0.01
	and l_quantity < 24;

select
	sum(l_extendedprice * l_discount) as revenue
from
	lineitem
where
	l_shipdate >= date '1994-01-01'
	and l_shipdate < date '1994-01-01' + interval '2' year
	and l_discount between 0.05 - 0.02 and 0.05 + 0.01;

select
	avg(l_extendedprice * l_discount) as avgrevenue
from
	lineitem
where
	l_shipdate >= date '1993-01-01'
	and l_discount between 0.05 - 0.01 and 0.05 + 0.01
	and l_quantity > 23;

select
	sum(l_extendedprice * l_discount) as revenue,
	l_quantity
from
	lineitem
where
	l_shipdate >= date '1993-01-01'
	and l_shipdate < date '1993-01-01' + interval '1' year
group by
	l_quantity
order by
	l_quantity desc;

select
	supp_nation,
	cust_nation,
	l_year,
	sum(volume) as revenue
from
	(
		select
			n1.n_name as supp_nation,
			n2.n_name as cust_nation,
			extract(year from l_shipdate) as l_year,
			l_extendedprice * (1 - l_discount) as volume
		from
			supplier,
			lineitem,
			orders,
			customer,
			nation n1,
			nation n2
		where
			s_suppkey = l_suppkey
			and o_orderkey = l_orderkey
			and c_custkey = o_custkey
			and s_nationkey = n1.n_nationkey
			and c_nationkey = n2.n_nationkey
			and (
				(n1.n_name = 'IRAQ' and n2.n_name = 'GERMANY')
				or (n1.n_name = 'GERMANY' and n2.n_name = 'IRAQ')
			)
			and l_shipdate between date '1995-01-01' and date '1996-12-31'
	) as shipping
group by
	supp_nation,
	cust_nation,
	l_year
order by
	supp_nation,
	cust_nation,
	l_year;



select
	l_year,
	sum(volume) as revenue
from
	(
		select
			extract(year from l_shipdate) as l_year,
			cast (l_extendedprice * (1 - l_discount) as decimal(18,2) ) as volume
		from
			supplier,
			lineitem,
			orders,
			customer,
			nation n1,
			nation n2
		where
			s_suppkey = l_suppkey
			and o_orderkey = l_orderkey
			and c_custkey = o_custkey
			and s_nationkey = n1.n_nationkey
			and c_nationkey = n2.n_nationkey
			and (
				(n1.n_name = 'IRAQ' and n2.n_name = 'GERMANY')
				or (n1.n_name = 'GERMANY' and n2.n_name = 'IRAQ')
			)
	) as shipping
group by
	l_year
order by
	l_year;



select
	o_year,
	sum(case
		when nation = 'GERMANY' then volume
		else 0
	end) / sum(volume) as mkt_share
from
	(
		select
			extract(year from o_orderdate) as o_year,
			cast (l_extendedprice * (1 - l_discount) as decimal(18,2)) as volume,
			n2.n_name as nation
		from
			part,
			supplier,
			lineitem,
			orders,
			customer,
			nation n1,
			nation n2,
			region
		where
			p_partkey = l_partkey
			and s_suppkey = l_suppkey
			and l_orderkey = o_orderkey
			and o_custkey = c_custkey
			and c_nationkey = n1.n_nationkey
			and n1.n_regionkey = r_regionkey
			and r_name = 'EUROPE'
			and s_nationkey = n2.n_nationkey
			and o_orderdate between date '1995-01-01' and date '1996-12-31'
			and p_type = 'PROMO ANODIZED COPPER'
	) as all_nations
group by
	o_year
order by
	o_year;

select
	o_year,
	sum(case
		when nation = 'GERMANY' then volume
		else 0
	end) / sum(volume) as mkt_share
from
	(
		select
			extract(year from o_orderdate) as o_year,
			cast ( l_extendedprice * (1 - l_discount) as decimal(18,2)) as volume,
			n2.n_name as nation
		from
			part,
			supplier,
			lineitem,
			orders,
			customer,
			nation n1,
			nation n2,
			region
		where
			p_partkey = l_partkey
			and s_suppkey = l_suppkey
			and l_orderkey = o_orderkey
			and o_custkey = c_custkey
			and c_nationkey = n1.n_nationkey
			and n1.n_regionkey = r_regionkey
	) as all_nations
group by
	o_year
order by
	o_year;

select
	o_year,
	sum(case
		when nation = 'BRAZIL' then volume
		else 0
	end) / sum(volume) as mkt_share
from
	(
		select
			extract(year from o_orderdate) as o_year,
			cast ( l_extendedprice * (1 - l_discount) as decimal(18,2)) as volume,
			n2.n_name as nation
		from
			part,
			supplier,
			lineitem,
			orders,
			customer,
			nation n1,
			nation n2,
			region
		where
			p_partkey = l_partkey
			and s_suppkey = l_suppkey
			and l_orderkey = o_orderkey
			and o_custkey = c_custkey
			and c_nationkey = n1.n_nationkey
			and n1.n_regionkey = r_regionkey
			and r_name = 'AMERICA'
			and s_nationkey = n2.n_nationkey
	) as all_nations
group by
	o_year
order by
	o_year;

select
	o_year,
	sum(case
		when nation = 'INDONESIA' then volume
		else 0
	end) / sum(volume) as mkt_share
from
	(
		select
			extract(year from o_orderdate) as o_year,
			cast (l_extendedprice * (1 - l_discount) as decimal(18,2)) as volume,
			n2.n_name as nation
		from
			part,
			supplier,
			lineitem,
			orders,
			customer,
			nation n1,
			nation n2,
			region
		where
			p_partkey = l_partkey
			and s_suppkey = l_suppkey
			and l_orderkey = o_orderkey
			and o_custkey = c_custkey
			and p_type = 'STANDARD POLISHED BRASS'
	) as all_nations
group by
	o_year
order by
	o_year;

select
	o_year,
	sum(case
		when nation = 'INDONESIA' then volume
		else 0
	end) / sum(volume) as mkt_share
from
	(
		select
			extract(year from o_orderdate) as o_year,
			cast ( l_extendedprice * (1 - l_discount) as decimal(18,2)) as volume,
			n2.n_name as nation
		from
			part,
			supplier,
			lineitem,
			orders,
			customer,
			nation n1,
			nation n2,
			region
		where
			p_partkey = l_partkey
			and s_suppkey = l_suppkey
			and l_orderkey = o_orderkey
			and o_custkey = c_custkey
			and p_type = 'SMALL PLATED COPPER'
	) as all_nations
group by
	o_year
order by
	o_year;

select
	nation,
	sum(case
		when nation = 'GERMANY' then volume
		else 0
	end) / sum(volume) as mkt_share
from
	(
		select
			extract(year from o_orderdate) as o_year,
			cast ( l_extendedprice * (1 - l_discount) as decimal(18,2)) as volume,
			n2.n_name as nation
		from
			part,
			supplier,
			lineitem,
			orders,
			customer,
			nation n1,
			nation n2,
			region
		where
			p_partkey = l_partkey
			and s_suppkey = l_suppkey
			and l_orderkey = o_orderkey
			and o_custkey = c_custkey
			and c_nationkey = n1.n_nationkey
			and n1.n_regionkey = r_regionkey
			and r_name = 'EUROPE'
			and s_nationkey = n2.n_nationkey
			and o_orderdate between date '1994-01-01' and date '1996-12-31'
			and p_type = 'SMALL BURNISHED STEEL'
	) as all_nations
group by
	nation
order by
	nation;



select
	nation,
	o_year,
	sum(amount) as sum_profit
from
	(
		select
			n_name as nation,
			extract(year from o_orderdate) as o_year,
			cast ( l_extendedprice * (1 - l_discount) as decimal(18,2)) - cast ( ps_supplycost * l_quantity as decimal(18,2)) as amount
		from
			part,
			supplier,
			lineitem,
			partsupp,
			orders,
			nation
		where
			s_suppkey = l_suppkey
			and ps_suppkey = l_suppkey
			and ps_partkey = l_partkey
			and p_partkey = l_partkey
			and o_orderkey = l_orderkey
			and s_nationkey = n_nationkey
			and p_name like '%slate%'
	) as profit
group by
	nation,
	o_year
order by
	nation,
	o_year desc;

select
	nation,
	o_year,
	sum(amount) as sum_profit
from
	(
		select
			n_name as nation,
			extract(year from o_orderdate) as o_year,
			cast ( l_extendedprice * (1 - l_discount) as decimal(18,2)) - cast ( ps_supplycost * l_quantity as decimal(18,2)) as amount
		from
			part,
			supplier,
			lineitem,
			partsupp,
			orders,
			nation
		where
			s_suppkey = l_suppkey
			and ps_suppkey = l_suppkey
			and ps_partkey = l_partkey
			and p_partkey = l_partkey
			and o_orderkey = l_orderkey
	) as profit
group by
	nation,
	o_year
order by
	nation,
	o_year desc;

select
	nation,
	sum(amount) as sum_profit
from
	(
		select
			n_name as nation,
			cast ( l_extendedprice * (1 - l_discount) as decimal(18,2)) -  cast ( ps_supplycost * l_quantity as decimal(18,2)) as amount
		from
			part,
			supplier,
			lineitem,
			partsupp,
			nation
		where
			s_suppkey = l_suppkey
			and ps_suppkey = l_suppkey
			and ps_partkey = l_partkey
			and p_partkey = l_partkey
			and p_name like '%lace%'
	) as profit
group by
	nation
order by
	nation;	



select
	c_custkey,
	c_name,
	sum(  cast ( l_extendedprice * (1 - l_discount) as decimal(18,2))  ) as revenue,
	c_acctbal,
	n_name,
	c_address,
	c_phone,
	c_comment
from
	customer,
	orders,
	lineitem,
	nation
where
	c_custkey = o_custkey
	and l_orderkey = o_orderkey
	and o_orderdate >= date '1994-10-01'
	and o_orderdate < date '1994-10-01' + interval '3' month
	and l_returnflag = 'R'
	and c_nationkey = n_nationkey
group by
	c_custkey,
	c_name,
	c_acctbal,
	c_phone,
	n_name,
	c_address,
	c_comment
order by
	revenue desc;


select
	c_custkey,
	c_name,
	sum(  cast ( l_extendedprice * (1 - l_discount) as decimal(18,2)) ) as revenue,
	c_acctbal,
	n_name,
	c_address,
	c_phone
from
	customer,
	orders,
	lineitem,
	nation
where
	c_custkey = o_custkey
	and l_orderkey = o_orderkey
group by
	c_custkey,
	c_name,
	c_acctbal,
	c_phone,
	n_name,
	c_address
order by
	revenue;





select
	c_custkey,
	c_name,
	sum( cast (l_extendedprice * (1 - l_discount)  as decimal(18,2)) ) as revenue,
	c_acctbal,
	n_name,
	c_address,
	c_phone
from
	customer,
	orders,
	lineitem,
	nation
where
	c_custkey = o_custkey
	and l_orderkey = o_orderkey
group by
	c_custkey,
	c_name,
	c_acctbal,
	n_name,
	c_address,
	c_phone
order by
	c_custkey;









select
	c_custkey,
	c_name,
	avg( cast (l_extendedprice * (1 - l_discount) as decimal(18,2) )) as revenue,
	c_acctbal,
	c_phone
from
	customer,
	orders,
	lineitem
where
	c_custkey = o_custkey
	and l_orderkey = o_orderkey
	and o_orderdate >= date '1994-10-01'
	and l_returnflag = 'R'
group by
	c_custkey,
	c_name,
	c_acctbal,
	c_phone
order by
	revenue desc;





select
	c_custkey,
	c_name,
	sum(cast ( l_extendedprice * (1 - l_discount) as decimal(18,2))  ) as revenue,
	c_acctbal
from
	customer,
	orders,
	lineitem,
	nation
where
	c_custkey = o_custkey
	and l_orderkey = o_orderkey
	and l_returnflag = 'N'
	and c_nationkey = n_nationkey
group by
	c_custkey,
	c_name,
	c_acctbal
order by
	revenue desc;



select
	l_shipmode,
	sum(case
		when o_orderpriority = '1-URGENT'
			or o_orderpriority = '2-HIGH'
			then 1
		else 0
	end) as high_line_count,
	sum(case
		when o_orderpriority <> '1-URGENT'
			and o_orderpriority <> '2-HIGH'
			then 1
		else 0
	end) as low_line_count
from
	orders,
	lineitem
where
	o_orderkey = l_orderkey
	and l_shipmode in ('SHIP', 'MAIL')
	and l_commitdate < l_receiptdate
	and l_shipdate < l_commitdate
	and l_receiptdate >= date '1995-01-01'
	and l_receiptdate < date '1995-01-01' + interval '1' year
group by
	l_shipmode
order by
	l_shipmode;

select
	l_shipmode,
	sum(case
		when o_orderpriority = '1-URGENT'
			or o_orderpriority = '3-MEDIUM'
			then 1
		else 0
	end) as high_line_count,
	sum(case
		when o_orderpriority <> '1-URGENT'
			and o_orderpriority <> '3-MEDIUM'
			then 1
		else 0
	end) as low_line_count
from
	orders,
	lineitem
where
	o_orderkey = l_orderkey
	and l_commitdate < l_receiptdate
	and l_shipdate < l_commitdate
group by
	l_shipmode
order by
	l_shipmode;

select
	l_shipmode,
	sum(case
		when o_orderpriority = '1-URGENT'
			or o_orderpriority = '2-HIGH'
			then 1
		else 0
	end) as high_line_count,
	sum(case
		when o_orderpriority <> '1-URGENT'
			and o_orderpriority <> '2-HIGH'
			then 1
		else 0
	end) as low_line_count
from
	orders,
	lineitem
where
	o_orderkey = l_orderkey
	and l_commitdate < l_receiptdate
	and l_shipdate < l_commitdate
group by
	l_shipmode
order by
	l_shipmode;

select
	l_shipmode,
	sum(case
		when o_orderpriority = '1-URGENT'
			then 1
		else 0
	end) as high_line_count,
	sum(case
		when o_orderpriority <> '1-URGENT'
			then 1
		else 0
	end) as low_line_count
from
	orders,
	lineitem
where
	o_orderkey = l_orderkey
	and l_shipmode in ('TRUCK', 'RAIL')
	and l_receiptdate >= date '1996-01-01'
	and l_receiptdate < date '1996-01-01' + interval '2' year
group by
	l_shipmode
order by
	l_shipmode;

select
	l_shipmode,
	sum(case
		when o_orderpriority = '3-MEDIUM'
			then 1
		else 0
	end) as high_line_count,
	sum(case
		when o_orderpriority <> '3-MEDIUM'
			then 1
		else 0
	end) as low_line_count
from
	orders,
	lineitem
where
	o_orderkey = l_orderkey
	and l_shipmode in ('TRUCK', 'RAIL')
group by
	l_shipmode
order by
	l_shipmode desc;

select
	l_shipmode,
	sum(case
		when o_orderpriority = '2-HIGH'
			then 1
		else 0
	end) as high_line_count,
	sum(case
		when o_orderpriority <> '2-HIGH'
			then 1
		else 0
	end) as low_line_count
from
	orders,
	lineitem
where
	o_orderkey = l_orderkey
	and l_receiptdate >= date '1992-01-01'
	and l_receiptdate < date '1995-01-01' + interval '1' year
group by
	l_shipmode
order by
	l_shipmode;

select
	c_count,
	count(*) as custdist
from
	(
		select
			c_custkey,
			count(o_orderkey)
		from
			customer left outer join orders on
				c_custkey = o_custkey
				and o_comment not like '%unusual%accounts%'
		group by
			c_custkey
	) as c_orders (c_custkey, c_count)
group by
	c_count
order by
	custdist desc,
	c_count desc;

select
	c_count,
	count(*) as custdist
from
	(
		select
			c_custkey,
			count(o_orderkey)
		from
			customer left outer join orders on
				c_custkey = o_custkey
		group by
			c_custkey
	) as c_orders (c_custkey, c_count)
group by
	c_count
order by
	custdist desc,
	c_count desc;



select
	c_count,
	count(*) as custdist
from
	(
		select
			c_custkey,
			count(o_orderkey)
		from
			customer left outer join orders on
				c_custkey = o_custkey
				and o_comment like '%unusual%accounts%'
		group by
			c_custkey
	) as c_orders (c_custkey, c_count)
group by
	c_count
order by
	custdist desc;



select
	c_count,
	count(*) as custdist
from
	(
		select
			c_custkey,
			count(o_orderkey)
		from
			customer left outer join orders on
				c_custkey = o_custkey
				and o_comment like '%unusual%accounts%'
		group by
			c_custkey
	) as c_orders (c_custkey, c_count)
group by
	c_count
order by
	c_count desc;



select
	c_count,
	count(*) as custdist
from
	(
		select
			c_custkey,
			count(o_orderkey)
		from
			customer left outer join orders on
				c_custkey = o_custkey
				and o_comment not like '%unusual%accounts%'
		group by
			c_custkey
	) as c_orders (c_custkey, c_count)
group by
	c_count
order by
	c_count desc;




select
	100.00 * sum(case
		when p_type like 'PROMO%'
			then cast(l_extendedprice * (1 - l_discount) as decimal(18,2))
		else 0
	end) / sum( cast(l_extendedprice * (1 - l_discount) as decimal(18,2)) ) as promo_revenue
from
	lineitem,
	part
where
	l_partkey = p_partkey
	and l_shipdate >= date '1995-07-01'
	and l_shipdate < date '1995-07-01' + interval '1' month;





select
	100.00 * sum(case
		when p_type like 'LARGE%'
			then cast ( l_extendedprice * (1 - l_discount) as decimal(18,2) ) as 
		else 0
	end) / sum( cast (l_extendedprice * (1 - l_discount) as decimal(18,2)) ) as promo_revenue
from
	lineitem,
	part
where
	l_partkey = p_partkey
	and l_shipdate >= date '1995-07-01';



select
	100.00 * sum(case
		when p_type like 'SMALL%'
			then cast ( l_extendedprice * (1 - l_discount) as decimal(18,2))
		else 0
	end) / sum( cast( l_extendedprice * (1 - l_discount) as decimal(18,2)) ) as promo_revenue
from
	lineitem,
	part
where
	l_partkey = p_partkey;



create view revenue0 (supplier_no, total_revenue) as
	select
		l_suppkey,
		sum( cast (l_extendedprice * (1 - l_discount)   as decimal(18,2)) )
	from
		lineitem
	where
		l_shipdate >= date '1997-04-01'
		and l_shipdate < date '1997-04-01' + interval '3' month
	group by
		l_suppkey;


select
	s_suppkey,
	s_name,
	s_address,
	s_phone,
	total_revenue
from
	supplier,
	revenue0
where
	s_suppkey = supplier_no
	and total_revenue = (
		select
			max(total_revenue)
		from
			revenue0
	)
order by
	s_suppkey;

select
	s_suppkey,
	total_revenue
from
	supplier,
	revenue0
where
	s_suppkey = supplier_no
	and total_revenue = (
		select
			max(total_revenue)
		from
			revenue0
	)
order by
	s_suppkey;

drop view revenue0;

create view revenue0 (supplier_no, total_revenue) as
	select
		l_suppkey,
		sum( cast ( l_extendedprice * (1 - l_discount) as decimal (18,2)) )
	from
		lineitem
	where
		l_shipdate >= date '1997-04-01'
		and l_shipdate < date '1997-04-01' + interval '3' month
	group by
		l_suppkey;

select
	s_suppkey,
	s_name,
	s_address,
	s_phone,
	total_revenue
from
	supplier,
	revenue0
where
	s_suppkey = supplier_no
	and total_revenue = (
		select
			max(total_revenue)
		from
			revenue0
	)
order by
	s_name;

select
	s_suppkey,
	s_name,
	s_address,
	s_phone,
	total_revenue
from
	supplier,
	revenue0
where
	s_suppkey = supplier_no
	and total_revenue = (
		select
			avg(total_revenue)
		from
			revenue0
	)
order by
	s_phone;

drop view revenue0;

select
	p_brand,
	p_type,
	p_size,
	count(distinct ps_suppkey) as supplier_cnt
from
	partsupp,
	part
where
	p_partkey = ps_partkey
	and p_brand <> 'Brand#23'
	and p_type not like 'PROMO BRUSHED%'
	and p_size in (41, 6, 36, 45, 30, 24, 27, 16)
	and ps_suppkey not in (
		select
			s_suppkey
		from
			supplier
		where
			s_comment like '%Customer%Complaints%'
	)
group by
	p_brand,
	p_type,
	p_size
order by
	supplier_cnt desc,
	p_brand,
	p_type,
	p_size;

select
	p_size,
	count(distinct ps_suppkey) as supplier_cnt
from
	partsupp,
	part
where
	p_partkey = ps_partkey
	and p_size in (4, 23, 45, 30, 24, 27, 13)
	and ps_suppkey not in (
		select
			s_suppkey
		from
			supplier
		where
			s_comment like '%Customer%Complaints%'
	)
group by
	p_size
order by
	supplier_cnt,
	p_size;

select
	p_type,
	p_size,
	count(distinct ps_suppkey) as supplier_cnt
from
	partsupp,
	part
where
	p_partkey = ps_partkey
	and p_type not like 'MEDIUM BURNISHED%'
	and p_size in (41, 6, 36, 45, 23, 13)
group by
	p_type,
	p_size
order by
	supplier_cnt desc,
	p_size;

select
	p_type,
	p_size,
	count(distinct ps_suppkey) as supplier_cnt
from
	partsupp,
	part
where
	p_partkey = ps_partkey
	and p_type not like 'MEDIUM BURNISHED%'
group by
	p_type,
	p_size
order by
	supplier_cnt desc;


select
	p_brand,
	count(distinct ps_suppkey) as supplier_cnt
from
	partsupp,
	part
where
	p_partkey = ps_partkey
	and p_brand <> 'Brand#23'
	and ps_suppkey not in (
		select
			s_suppkey
		from
			supplier
		where
			s_comment like '%Customer%Complaints%'
	)
group by
	p_brand
order by
	supplier_cnt desc,
	p_brand;

select
	sum(l_extendedprice) / 7.0 as avg_yearly
from
	lineitem,
	part
where
	p_partkey = l_partkey
	and p_brand = 'Brand#35'
	and p_container = 'WRAP CASE'
	and l_quantity < (
		select
			0.2 * avg(l_quantity)
		from
			lineitem
		where
			l_partkey = p_partkey
	);

select
	sum(l_extendedprice) / 7.0 as avg_yearly
from
	lineitem,
	part
where
	p_partkey = l_partkey
	and l_quantity < (
		select
			0.2 * avg(l_quantity)
		from
			lineitem
		where
			l_partkey = p_partkey
	);

select
	sum(l_extendedprice) / 7.0 as avg_yearly
from
	lineitem,
	part
where
	p_partkey = l_partkey
	and p_brand = 'Brand#23'
	and l_quantity > (
		select
			0.2 * avg(l_quantity)
		from
			lineitem
		where
			l_partkey = p_partkey
	);

select
	sum(l_extendedprice) / 7.0 as avg_yearly
from
	lineitem,
	part
where
	p_partkey = l_partkey
	and p_brand = 'Brand#35'
	and p_container = 'LG BOX';

select
	c_name,
	c_custkey,
	o_orderkey,
	o_orderdate,
	o_totalprice,
	sum(l_quantity)
from
	customer,
	orders,
	lineitem
where
	o_orderkey in (
		select
			l_orderkey
		from
			lineitem
		group by
			l_orderkey having
				sum(l_quantity) > 315
	)
	and c_custkey = o_custkey
	and o_orderkey = l_orderkey
group by
	c_name,
	c_custkey,
	o_orderkey,
	o_orderdate,
	o_totalprice
order by
	o_totalprice desc,
	o_orderdate;

select
	o_orderdate,
	o_totalprice,
	max(l_quantity)
from
	customer,
	orders,
	lineitem
where
	o_orderkey in (
		select
			l_orderkey
		from
			lineitem
		group by
			l_orderkey having
				max(l_quantity) > 40
	)
	and c_custkey = o_custkey
	and o_orderkey = l_orderkey
group by
	o_orderdate,
	o_totalprice
order by
	o_totalprice desc,
	o_orderdate;

select
	o_orderkey,
	o_orderdate,
	o_totalprice,
	avg(l_quantity)
from
	orders,
	lineitem
where
	o_orderkey in (
		select
			l_orderkey
		from
			lineitem
		group by
			l_orderkey having
				avg(l_quantity) > 30
	)
	and o_orderkey = l_orderkey
group by
	o_orderkey,
	o_orderdate,
	o_totalprice
order by
	o_totalprice,
	o_orderdate;

select
	c_custkey,
	o_orderkey,
	o_totalprice,
	min(l_quantity)
from
	customer,
	orders,
	lineitem
where
	c_custkey = o_custkey
	and o_orderkey = l_orderkey
group by
	c_custkey,
	o_orderkey,
	o_totalprice
order by
	o_totalprice desc;




select
	sum( cast (l_extendedprice* (1 - l_discount) as decimal(18,2)) ) as revenue
from
	lineitem,
	part
where
	(
		p_partkey = l_partkey
		and p_brand = 'Brand#51'
		and p_container in ('SM CASE', 'SM BOX', 'SM PACK', 'SM PKG')
		and l_quantity >= 5 and l_quantity <= 5 + 10
		and p_size between 1 and 5
		and l_shipmode in ('AIR', 'AIR REG')
		and l_shipinstruct = 'DELIVER IN PERSON'
	)
	or
	(
		p_partkey = l_partkey
		and p_brand = 'Brand#45'
		and p_container in ('MED BAG', 'MED BOX', 'MED PKG', 'MED PACK')
		and l_quantity >= 12 and l_quantity <= 12 + 10
		and p_size between 1 and 10
		and l_shipmode in ('AIR', 'AIR REG')
		and l_shipinstruct = 'DELIVER IN PERSON'
	)
	or
	(
		p_partkey = l_partkey
		and p_brand = 'Brand#33'
		and p_container in ('LG CASE', 'LG BOX', 'LG PACK', 'LG PKG')
		and l_quantity >= 23 and l_quantity <= 23 + 10
		and p_size between 1 and 15
		and l_shipmode in ('AIR', 'AIR REG')
		and l_shipinstruct = 'DELIVER IN PERSON'
	);

select
	max( cast ( l_extendedprice* (1 - l_discount) as decimal(18,2)) ) as maxrevenue
from
	lineitem,
	part
where
	(
		p_partkey = l_partkey
		and p_brand = 'Brand#55'
		and p_size between 1 and 5
		and l_shipmode in ('AIR', 'AIR REG')
	)
	or
	(
		p_partkey = l_partkey
		and p_brand = 'Brand#45'
		and p_container in ('MED BAG', 'MED BOX', 'MED PKG', 'MED PACK')
		and p_size between 1 and 10
	)
	or
	(
		p_partkey = l_partkey
		and p_container in ('LG CASE', 'LG BOX', 'LG PACK', 'LG PKG')
		and l_quantity >= 23 and l_quantity <= 23 + 10
		and l_shipinstruct = 'DELIVER IN PERSON'
	);

select
	avg( cast (l_extendedprice* (1 - l_discount) as decimal(18,2)) ) as avgrevenue
from
	lineitem,
	part
where
	(
		p_partkey = l_partkey
		and p_brand = 'Brand#23'
		and p_container in ('SM CASE', 'SM BOX', 'SM PACK', 'SM PKG')
		and l_quantity >= 5 and l_quantity <= 5 + 10
		and p_size between 1 and 10
		and l_shipmode in ('TRUCK', 'MAIL')
		and l_shipinstruct = 'TAKE BACK RETURN'
	);

select
	min( cast(l_extendedprice* (1 - l_discount)as decimal(18,2))) as minrevenue
from
	lineitem,
	part
where
	(
		p_partkey = l_partkey
		and l_quantity >= 5 and l_quantity <= 5 + 20
		and l_shipmode in ('SHIP', 'RAIL')
		and l_shipinstruct in ('DELIVER IN PERSON', 'TAKE BACK RETURN')
	)
	or
	(
		p_partkey = l_partkey
		and p_brand = 'Brand#13'
		and p_container in ('JUMBO BAG', 'MED CASE')
	);

select
	min( cast ( l_extendedprice* (1 - l_discount) as decimal(18,2)) ) as minrevenue
from
	lineitem,
	part
where
	(
		p_partkey = l_partkey
		and p_brand = 'Brand#51'
		and p_container in ('SM CASE', 'SM BOX', 'SM PACK', 'SM PKG')
		and l_quantity >= 5 and l_quantity <= 5 + 20
		and p_size between 12 and 25
		and l_shipmode in ('SHIP', 'RAIL')
		and l_shipinstruct in ('DELIVER IN PERSON', 'TAKE BACK RETURN')
	)
	or
	(
		p_partkey = l_partkey
		and p_brand = 'Brand#13'
		and p_container in ('JUMBO BAG', 'MED CASE')
		and p_size between 1 and 30
	);

select
	s_name,
	s_address
from
	supplier,
	nation
where
	s_suppkey in (
		select
			ps_suppkey
		from
			partsupp
		where
			ps_partkey in (
				select
					p_partkey
				from
					part
				where
					p_name like 'blanched%'
			)
			and ps_availqty > (
				select
					0.5 * sum( cast(l_quantity as decimal(18,2)) )
				from
					lineitem
				where
					l_partkey = ps_partkey
					and l_suppkey = ps_suppkey
					and l_shipdate >= date '1996-01-01'
					and l_shipdate < date '1996-01-01' + interval '1' year
			)
	)
	and s_nationkey = n_nationkey
	and n_name = 'RUSSIA'
order by
	s_name;

select
	s_name,
	s_address
from
	supplier,
	nation
where
	s_suppkey in (
		select
			ps_suppkey
		from
			partsupp
		where
			ps_partkey in (
				select
					p_partkey
				from
					part
				where
					p_name like 'blanched%'
			)
			and ps_availqty > (
				select
					0.5 * sum( cast(l_quantity as decimal(18,2)) )
				from
					lineitem
				where
					l_partkey = ps_partkey
					and l_suppkey = ps_suppkey
					and l_shipdate < date '1996-01-01' + interval '1' year
			)
	)
	and s_nationkey = n_nationkey
	and n_name = 'INDIA'
order by
	s_name;

select
	s_name,
	s_address
from
	supplier,
	nation
where
	s_suppkey in (
		select
			ps_suppkey
		from
			partsupp
		where
			ps_partkey in (
				select
					p_partkey
				from
					part
				where
					p_name like 'cornflower%'
			)
			and ps_availqty > (
				select
					0.5 * avg(l_quantity)
				from
					lineitem
				where
					l_partkey = ps_partkey
					and l_suppkey = ps_suppkey
			)
	)
	and s_nationkey = n_nationkey
	and n_name = 'IRAN'
order by
	s_name;

select
	s_name,
	s_address
from
	supplier,
	nation
where
	s_suppkey in (
		select
			ps_suppkey
		from
			partsupp
		where
			ps_partkey in (
				select
					p_partkey
				from
					part
				where
					p_name like 'chocolate%'
			)
			and ps_availqty > (
				select
					0.5 * avg(l_quantity)
				from
					lineitem
				where
					l_partkey = ps_partkey
					and l_suppkey = ps_suppkey
			)
	)
	and s_nationkey = n_nationkey
	and n_name = 'CHINA'
order by
	s_address;

select
	s_name,
	s_address
from
	supplier,
	nation
where
	s_suppkey in (
		select
			ps_suppkey
		from
			partsupp
		where
			ps_availqty > (
				select
					0.5 * sum(l_quantity)
				from
					lineitem
				where
					l_partkey = ps_partkey
					and l_suppkey = ps_suppkey
					and l_shipdate >= date '1994-05-01'
			)
	)
	and s_nationkey = n_nationkey
	and n_name = 'PERU'
order by
	s_name;

select
	s_name,
	s_address
from
	supplier,
	nation
where
	s_suppkey in (
		select
			ps_suppkey
		from
			partsupp
		where
			ps_partkey in (
				select
					p_partkey
				from
					part
				where
					p_name like 'saddle%'
			)
			and ps_availqty > (
				select
					0.5 * sum(l_quantity)
				from
					lineitem
				where
					l_partkey = ps_partkey
					and l_suppkey = ps_suppkey
					and l_shipdate >= date '1996-01-01'
					and l_shipdate < date '1996-01-01' + interval '2' year
			)
	)
order by
	s_name;

select
	s_name,
	s_address
from
	supplier,
	nation
where
	s_suppkey in (
		select
			ps_suppkey
		from
			partsupp
		where
			ps_partkey in (
				select
					p_partkey
				from
					part
				where
					p_name like 'rose%'
			)
	)
	and s_nationkey = n_nationkey
	and n_name = 'INDONESIA'
order by
	s_name;

select
	s_name,
	count(*) as numwait
from
	supplier,
	lineitem l1,
	orders,
	nation
where
	s_suppkey = l1.l_suppkey
	and o_orderkey = l1.l_orderkey
	and o_orderstatus = 'F'
	and l1.l_receiptdate > l1.l_commitdate
	and exists (
		select
			*
		from
			lineitem l2
		where
			l2.l_orderkey = l1.l_orderkey
			and l2.l_suppkey <> l1.l_suppkey
	)
	and not exists (
		select
			*
		from
			lineitem l3
		where
			l3.l_orderkey = l1.l_orderkey
			and l3.l_suppkey <> l1.l_suppkey
			and l3.l_receiptdate > l3.l_commitdate
	)
	and s_nationkey = n_nationkey
	and n_name = 'PERU'
group by
	s_name
order by
	numwait desc,
	s_name;

select
	s_name,
	count(*) as numwait
from
	supplier,
	lineitem l1,
	orders,
	nation
where
	s_suppkey = l1.l_suppkey
	and o_orderkey = l1.l_orderkey
	and o_orderstatus = 'F'
	and l1.l_receiptdate > l1.l_commitdate
	and exists (
		select
			*
		from
			lineitem l2
		where
			l2.l_orderkey = l1.l_orderkey
			and l2.l_suppkey <> l1.l_suppkey
	)
	and s_nationkey = n_nationkey
	and n_name = 'PERU'
group by
	s_name
order by
	numwait desc,
	s_name;

select
	s_name,
	count(*) as numwait
from
	supplier,
	lineitem l1,
	orders
where
	s_suppkey = l1.l_suppkey
	and o_orderkey = l1.l_orderkey
	and o_orderstatus = 'O'
	and not exists (
		select
			*
		from
			lineitem l3
		where
			l3.l_orderkey = l1.l_orderkey
			and l3.l_suppkey <> l1.l_suppkey
	)
group by
	s_name
order by
	s_name;

select
	s_name,
	count(*) as numwait
from
	supplier,
	lineitem l1,
	orders,
	nation
where
	s_suppkey = l1.l_suppkey
	and o_orderkey = l1.l_orderkey
	and o_orderstatus = 'F'
	and exists (
		select
			*
		from
			lineitem l2
		where
			l2.l_orderkey = l1.l_orderkey
	)
	and not exists (
		select
			*
		from
			lineitem l3
		where
			l3.l_orderkey = l1.l_orderkey
			and l3.l_receiptdate > l3.l_commitdate
	)
	and s_nationkey = n_nationkey
	and n_name = 'UNITED STATES'
group by
	s_name
order by
	numwait desc,
	s_name;

select
	cntrycode,
	count(*) as numcust,
	sum(c_acctbal) as totacctbal
from
	(
		select
			substring(c_phone from 1 for 2) as cntrycode,
			c_acctbal
		from
			customer
		where
			substring(c_phone from 1 for 2) in
				('32', '24', '21', '23', '17', '20', '22')
			and c_acctbal > (
				select
					avg(c_acctbal)
				from
					customer
				where
					c_acctbal > 0.00
					and substring(c_phone from 1 for 2) in
						('32', '24', '21', '23', '17', '20', '22')
			)
			and not exists (
				select
					*
				from
					orders
				where
					o_custkey = c_custkey
			)
	) as custsale
group by
	cntrycode
order by
	cntrycode;

select
	cntrycode,
	count(*) as numcust,
	sum(c_acctbal) as totacctbal
from
	(
		select
			substring(c_phone from 1 for 2) as cntrycode,
			c_acctbal
		from
			customer
		where
			substring(c_phone from 1 for 2) in
				('32', '23', '13', '20', '27')
			and c_acctbal > (
				select
					avg(c_acctbal)
				from
					customer
				where
					c_acctbal > 0.00
					and substring(c_phone from 1 for 2) in
						('32', '23', '13', '20', '27')
			)
			and not exists (
				select
					*
				from
					orders
				where
					o_custkey = c_custkey
			)
	) as custsale
group by
	cntrycode
order by
	cntrycode;

select
	cntrycode,
	count(*) as numcust,
	sum(c_acctbal) as totacctbal
from
	(
		select
			substring(c_phone from 1 for 2) as cntrycode,
			c_acctbal
		from
			customer
		where
			substring(c_phone from 1 for 2) in
				('32', '23', '13', '20', '27')
			and c_acctbal < (
				select
					avg(c_acctbal)
				from
					customer
				where
					c_acctbal > 0.00
					and substring(c_phone from 1 for 2) in
						('32', '23', '13', '20', '27')
			)
	) as custsale
group by
	cntrycode
order by
	cntrycode;

select
	cntrycode,
	count(*) as numcust,
	max(c_acctbal) as totacctbal
from
	(
		select
			substring(c_phone from 1 for 2) as cntrycode,
			c_acctbal
		from
			customer
		where
			substring(c_phone from 1 for 2) in
				('13')
			and c_acctbal > (
				select
					avg(c_acctbal)
				from
					customer
				where
					c_acctbal > 0.00
					and substring(c_phone from 1 for 2) in
						('13')
			)
			and not exists (
				select
					*
				from
					orders
				where
					o_custkey = c_custkey
			)
	) as custsale
group by
	cntrycode
order by
	cntrycode;


select
	100.00 * sum(case
		when p_type not like 'PROMO%'
			then cast ( l_extendedprice * (1 - l_discount) as decimal(18,2))
		else 0
	end) / sum( cast(l_extendedprice * (1 - l_discount) as decimal(18,2)) ) as promo_revenue
from
	lineitem,
	part
where
	l_partkey = p_partkey
	and l_shipdate < date '1995-07-01' + interval '1' month;






select
	100.00 * sum(case
		when p_type not like 'PROMO%'
			then cast( l_extendedprice * (1 - l_discount) as decimal (18,2))
		else 0
	end) / sum( cast (l_extendedprice * (1 - l_discount) as decimal(18,2) )) as promo_revenue
from
	lineitem,
	part
where
	l_partkey = p_partkey;



