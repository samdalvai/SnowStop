from bs4 import BeautifulSoup
import requests
import re
import time
# coding: latin1

#url = ("https://www.flender-flux.de/schneefang")
#print("URL: " + url)

def hasNumbers(inputString):
    return any(char.isdigit() for char in inputString)

def get_material(index):
    if index == 0:
        return "V"
    if index == 1:
        return "B"
    if index == 2:
        return "K"
    if index == 3:
        return "A"
    if index == 4:
        return "E"
    

page = requests.get("https://www.flender-flux.de/schneefang")
soup = BeautifulSoup(page.content, 'html.parser')

items = soup.find_all(class_="single-product-box")

out = open('./flender.json', 'w', encoding='utf-8')
out.write('{' + "\n" + "\t" + "\"articles\": [" + "\n")
out.close

for x in range(0,93):
    # 0 to 93!!
    current = items[x]
    #print(tonight.prettify())

    name = current.find(itemprop="name").get_text()
    print("Name: " + name)

    rows = current.find_all('tr')
    #print("length: " + str(len(rows)))

    for i in range (1, (len(rows) - 2)):
        #print(rows[i].prettify())
        
        # find article
        article = rows[i].find(scope="row").get_text().replace("\n","").replace("\t","").replace(" ","")
        print("Article: " + article)

        # find price
        price = rows[i].find(class_="show_price text-right").get_text().replace("\n","").replace("\t","").replace(" ","")
        print("Price: " + price)

        columns = rows[i].find_all('td')
        
        height_diam = columns[0].get_text().replace("\n","").replace("\t","").replace(" ","")

        if height_diam != "":
            print("Height-Diam: " + height_diam)

        measure = columns[1].get_text().replace("\n","").replace("\t","").replace(" ","")

        if measure != "":
            print("Measure: " + measure)

        empty_col = 0
        material = ""

        for j in range(0,(len(columns))):
            #print("columns: " + str(len(columns)))
            is_material = columns[j].find_all('i')
            #print("has material: " + str(len(is_material)))
            if ((len(is_material) == 1)):
                material = get_material(empty_col)
                print("Material: " + material)

            text = columns[j].get_text().replace("\n","").replace("\t","").replace(" ","")

            if text == "":
                empty_col += 1

        print("")

        # translate from german to english
        if material == "A":
            material = "Aluminium"
        elif material == "B":
            material = "Painted Steel"
        elif material == "V":
            material = "Zink Steel"
        elif material == "K":
            material = "Copper"
        elif material == "E":
            material = "Stainless Steel"

        name = name.replace("Rohren","Tubes")
        name = name.replace("Rohre","Tubes")
        name = name.replace("Rohr","Tube")
        name = name.replace("Schneefanggitter","Grid Retainer")
        name = name.replace("Schneefangstützen","Retainer Holder")
        name = name.replace("Schneefangstütze","Retainer Holder")
        name = name.replace("Verbindungsmuffen","Connection Sleeves")
        name = name.replace("für","for")
        name = name.replace("mit","with")
        name = name.replace("Eishalter","Ice Retainer")
        name = name.replace("Aufstockelement","Clamp Raiser")
        name = name.replace("Schneefanglasche","Snowstop Clamp")
        name = name.replace("hoch","tall")
        name = name.replace("Schneefang-Komplettelement","Snowstop-Complete Set")
        name = name.replace("Schneefang-Komplettset","Snowstop-Complete Set")
        name = name.replace("Gitter","Grid")
        name = name.replace("Verlängerung","Extension")
        name = name.replace("profiliertes","Profiled")
        name = name.replace("aus","in")
        name = name.replace("Blech","Metal")
        name = name.replace("Endkappen","End Caps")
        name = name.replace("Ø","")

        height_diam = height_diam.replace("f.profiliertesSchneefanggitterAlu-Zink","for profiled Grid Retainer")
        height_diam = height_diam.replace("beschichtet","painted")
        height_diam = height_diam.replace("zumAnklemmen","for nailing")
        height_diam = height_diam.replace("zumEinhängen","for hanging")
        height_diam = height_diam.replace("Alu-RohrØ32","for Aluminium Tube 32 mm")
        height_diam = height_diam.replace("Alu-Rohr32Ø","for Aluminium Tube 32 mm")
        height_diam = height_diam.replace("Rundholz/-rohrbis","for wooden log/-Tube up to ")
        height_diam = height_diam.replace("Ø","")
        
        price = price.replace("€","")

        if material.strip() == "":
            material = "PVC"

        out.write("\t" + "{" + "\n")

        out.write("\t\t" + "\"name\": " + "\"" + name.replace("\n","") + "\"" + "," + "\n")
        out.write("\t\t" + "\"article\": " + "\"" + article.replace("\n","") + "\"" + "," + "\n")
        out.write("\t\t" + "\"feature\": " + "\"" + height_diam.replace("\n","") + "\"" + "," + "\n")
        out.write("\t\t" + "\"measure\": " + "\"" + measure.replace("\n","") + "\"" + "," + "\n")
        out.write("\t\t" + "\"material\": " + "\"" + material.replace("\n","") + "\"" + "," + "\n")

        out.write("\t\t" + "\"price\": " + "\"" + price.replace("\n","").replace(",",".") + "\"" + "\n")

        if article.strip() == "030420":
            out.write("\t}" + "\n")
        else:
            out.write("\t}," + "\n")

out = open('./flender.json', 'a', encoding='utf-8')
out.write("\t" + "]" + "\n")
out.write("}")
out.close