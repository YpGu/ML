import sys

if __name__ == '__main__':

	if len(sys.argv) != 2:
		print 'Usage: python select.py <num of top words>'
		sys.exit(0)

	thr = int(sys.argv[1])
	print 'Extracting top ' + str(thr) + ' word(s)'

	fin = open('train.data')
	wfreq = {}
	lines = fin.readlines()
	for l in lines:
		docID, termID, freq = l.split(' ')
		freq = int(freq)
		if termID in wfreq:
			wfreq[termID] += freq
		else:
			wfreq[termID] = freq
	fin.close()

	candidate = []
	for x in sorted(wfreq.items(), key = lambda x: x[1], reverse = True):
		candidate.append(x[0])
		if len(candidate) == thr:
			break
#	print candidate

	fout = open('./wordList/wordList' + str(thr), 'w')
	for x in candidate:
		newline = x + '\n'
		fout.write(newline)
	fout.close()

