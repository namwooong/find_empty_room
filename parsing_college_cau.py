import urllib.request
import urllib.parse
import urllib
import json
from bs4 import BeautifulSoup
from urllib.request import Request, urlopen
from gzip import GzipFile
from io import StringIO
import zlib

file_cau_college=open("cau_college.txt",'w')

url="https://everytime.kr/find/timetable/subject/major/list"


data=urllib.parse.urlencode({'year':'2018','semester':'1'})
data = data.encode('utf-8')

#you should put your every time cookies
answer=Request(url,headers={'Content-Length':'20','Connection':'keep-alive','Accept-Language':'ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7','Accept-Encoding':'gzip, deflate, br','Origin':'https://everytime.kr','Host':'everytime.kr','Content-Type':'application/x-www-form-urlencoded; charset=UTF-8','Accept':'*/*','Referer':'https://everytime.kr/timetable/','X-Requested-With': 'XMLHttpRequest','User-Agent':'Mozilla/5.0','Cookie':''})


text_anwer=urlopen(answer, data)
de=zlib.decompress(text_anwer.read(),16+zlib.MAX_WBITS)
#print(de)


soup=BeautifulSoup(de,'html.parser')

campus_list=soup.find_all('campus')

for j in campus_list:
	if j['id']!="2":
		continue
	college_list=j.find_all('college')

	for i in college_list:
		print(i['id'])
		file_cau_college.write(str(i['id'])+'\n')
		major_list=i.find_all('major')
		for k in major_list:
			print(k['id'])
			file_cau_college.write(str(k['id']+'\n'))
		file_cau_college.write('#'+'\n')



