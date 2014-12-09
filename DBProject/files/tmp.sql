  select
	sum(  cast (l_extendedprice* (1 - l_discount) as decimal(18,2)) ) as revenue
from
	lineitem,
	part
where
	(
		p_partkey = l_partkey
		and p_container in ('SM CASE' 'SM BOX' 'SM PACK' 'SM PKG')
	)
	or
	(
		p_partkey = l_partkey
		and p_brand = 'Brand#45'
		and p_size between 1 and 10
		and l_shipmode in ('AIR' 'AIR REG')
	)
	or
	(
		p_partkey = l_partkey
		and p_brand = 'Brand#33'
		and p_container in ('LG CASE' 'LG BOX' 'LG PACK' 'LG PKG')
		and l_quantity >= 23 and l_quantity <= 23 + 10
	);
