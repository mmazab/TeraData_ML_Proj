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