import urllib.request
import urllib.parse
import urllib
import json
from bs4 import BeautifulSoup
from urllib.request import Request, urlopen
from operator import eq
#file_cau_college=open("ca")


file_result=open("cau_table.txt",'w')
file_open=open("cau_college.txt",'r')
count=0

while True:
	print(count)
	count=count+1
	line=file_open.readline()
	if not line: break
	url="https://everytime.kr/find/timetable/subject/list"
	index_college=line
	while True:
		next_line=file_open.readline()
		index_major=next_line
		if str(index_major)=="#\n" : 
			break
		
		print(index_college)
		print(index_major)
		data=urllib.parse.urlencode({'college':str(index_college),'major':str(index_major),'year':'2018','semester':'1','campus':'2'})
		data = data.encode('utf-8')

		#you should put your every time cookie
		answer=Request(url,headers={'X-Requested-With': 'XMLHttpRequest','Cookie':''})

		text_anwer=urlopen(answer, data).read().decode('utf-8')
		soup=BeautifulSoup(text_anwer,'html.parser')
		#print(text_anwer)
		time_list=soup.find_all('time')
		for i in time_list:
			#print(i)
			print(i['value'])
			file_result.write(str(i['value'])+'\n')
			list_data=i.find_all('data')
			for k in list_data:
				#print(k)
				#print(k['day'])
				#print(k['endtime'])
				endtime=k['endtime']
				starttime=k['starttime']
				day=k['day']





