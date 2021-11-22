from bs4 import BeautifulSoup
import requests
import re
import time
# coding: latin1


# return content of type specified for a search parameter
def scrapeWikiPage(searchParam,elementType,index):
    # replace whitespaces with underscore
    search = searchParam.replace(" ","_").replace("\n","")

    url = ("https://it.wikipedia.org/wiki/" + search)
    print("URL: " + url)

    # query website
    page = requests.get(url)
    soup = BeautifulSoup(page.content, 'html.parser')

    # find all elements of table and return
    try:
        nodes = soup.find_all(elementType)[index].getText()
        return nodes
    except IndexError as error:
        return ""

# extract string corresponding to regex from text
def extractRegexFromText(text,regex,default):
    match = re.search(regex, text)
    if match:
        string = match.group()
        print(string)
    else:
        print("not found")
        string = default ## default value in case not found
    return string
    

# get all the names of municipalities (open file with right encoding)
municipalities = open ("comuni.txt", "r", encoding='utf-8')

cities = municipalities.readlines()

out = open('./cities.json', 'a', encoding='utf-8')
out.write('{' + "\n" + "\t" + "\"cities\": [" + "\n")
out.close

for city in cities:

    nodes = scrapeWikiPage(city,'table',0)
    if "Altitudine" not in nodes:
        nodes = scrapeWikiPage(city,'table',1)
        if "Altitudine" not in nodes:
            nodes = scrapeWikiPage(city,'table',2)
            if "Altitudine" not in nodes:
                nodes = scrapeWikiPage((city + "_(Italia)"),'table',0)
                if "Altitudine" not in nodes:
                    nodes = scrapeWikiPage((city + "_(Italia)"),'table',1)
                    if "Altitudine" not in nodes:
                        nodes = scrapeWikiPage((city + "_(Italia)"),'table',2)
    
    #print(nodes)

    # find elevation in elements
    altitudeRaw = extractRegexFromText(nodes,r'Altitudine[0-9]+\s?([0-9]+)?',"")

    print("Altitude raw: " + altitudeRaw)

    capRow = extractRegexFromText(nodes,r'postale[0-9]{5}',"")

    provinceRaw = extractRegexFromText(nodes,r'Targa[A-Z][A-Z]',"NULL")

    altitude = re.sub("[^0-9]", "", altitudeRaw)

    if altitude == "":
        altitude = "-1"

    cap = re.sub("[^0-9]", "", capRow)

    if cap == "":
        cap = "NULL"

    # extract province
    province = provinceRaw.replace("Targa","")

    print("The altitude: " + altitude)

    # write to file
    out = open('./cities.json', 'a', encoding='utf-8')
    out.write("\t" + "{" + "\n")

    out.write("\t\t" + "\"name\": " + "\"" + city.replace("\n","") + "\"" + "," + "\n")
    out.write("\t\t" + "\"cap\": " + "\"" + cap + "\"" + "," + "\n")
    out.write("\t\t" + "\"province\": " + "\"" + province + "\"" + "," + "\n")
    out.write("\t\t" + "\"altitude\": " +  altitude + "\n")

    if "Villaspeciosa" in city:
        out.write("\t}" + "\n")
    else:
         out.write("\t}," + "\n")

    out.close

    time.sleep(0.05)

municipalities.close()

out = open('./cities.json', 'a', encoding='utf-8')
out.write("\t" + "]" + "\n")
out.write("}")
out.close
