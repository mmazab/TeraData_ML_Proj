
breakwords = {}

keycounters = {}





def main():
	i=1
	
	keycounters = {'select' : 0,
		'select_col':0,
		'order by':0,
		'order_att':0, 
		'from':0, 
		'from_att':0,	 
		'where':0, 
		'where_att':0,
		'group by':0,
		'group_att':0,
		'like':0, 
		'and':0, 
		'sum':0, 
		'count':0,
		'extract':0, 
		'when':0,
		'mul':0, 
		'avg':0,
		'eq':0,
		'leq':0,
		'meq':0,
		'div':0,
		'as':0,
		'min':0,
		'max':0,
		'drop':0}
	breakwords = {'select', 'from', 'where', 'group by', 'order by'}
	fileName = 'tmp.sql'
	fhand = open(fileName)
	
	#print "\n\nNow processing %s \n\n" % fileName
	count = 0
	count_att = ''
	line = ''
	for line in fhand:
		line = line.strip()
		count = count + 1
		if line.startswith(':'):
			pass
		elif line.startswith('sum'):
			keycounters['sum'] = keycounters['sum']+1 
		elif line.startswith('avg'):
			keycounters['avg'] = keycounters['avg']+1
		elif line.startswith('min'):
			keycounters['min'] = keycounters['min']+1
		elif line.startswith('max'):
			keycounters['max'] = keycounters['max']+1
		elif line.startswith('count'):
			keycounters['count'] = keycounters['count']+1
		elif line.startswith('extract'):
			keycounters['extract'] = keycounters['extract']+1
		elif line.startswith('drop'):
			keycounters['drop'] = keycounters['drop']+1
		
	
		if line == 'select':
			keycounters['select'] = keycounters['select']+1
			if count_att != '':
				keycounters[count_att] = keycounters[count_att]+count-1
			count = 0
			count_att='select_col'		
		
		if line == 'from':
			keycounters['from'] = keycounters['from']+1
			if count_att != '':
				keycounters[count_att] = keycounters[count_att]+count-1
			count = 0
			count_att='from_att'

		if line == 'group by':
			keycounters['group by'] = keycounters['group by']+1
			if count_att != '':
				keycounters[count_att] = keycounters[count_att]+count-1	
			count = 0
			count_att='group_att'

		if line == 'where':
			keycounters['where'] = keycounters['where']+1
			if count_att != '':
				keycounters[count_att] = keycounters[count_att]+count-1
			count = 0 
			count_att='where_att'

		if line == 'order by' :
			keycounters['order by'] = keycounters['order by']+1
			if count_att != '':
				keycounters[count_att] = keycounters[count_att]+count-1
			count = 0
			count_att='order_att'

		if '<=' in line:
			keycounters['leq'] = keycounters['leq']+1	
		elif '=<' in line:
			keycounters['meq'] = keycounters['meq']+1
		elif '=' in line:
			keycounters['eq'] = keycounters['eq']+1	
		if '*' in line and '*' != line:
			keycounters['mul'] = keycounters['mul']+1					

	if len(count_att) > 0:
		if line.startswith(':'):
			keycounters[count_att] = keycounters[count_att]+count-1
		elif len(line) > 0:
			keycounters[count_att] = keycounters[count_att]+count-1		
		else:
			keycounters[count_att] = keycounters[count_att]+count-1
	fhand.close()
		
	tmpFile = open("tmp.feat", "w")
	featureVector = ""
	feats = sorted(keycounters)
	for value in feats:
		featureVector = featureVector + str(keycounters[value]) + "\t"
	tmpFile.write(featureVector)


 
if __name__ == '__main__':     # if the function is the main function ...
    main() # ...call it







