import json
from random import randrange
# coding: latin1

def findCity(city):
    found = -1
    with open('../Json_Data/cities.json', encoding='utf-8') as json_file:
        data = json.load(json_file)

        for p in data['cities']:
            if (p['name'].replace("'","").lower() == city.replace("'","").lower()):
                if p['cap'] != "NULL" and p['province'] != "NULL":
                    found = 1
    
    return found

def findCityWithCap(cap,city):
    found = -1
    with open('../Json_Data/cities.json', encoding='utf-8') as json_file:
        data = json.load(json_file)

        for p in data['cities']:
            if (p['name'].replace("'","").lower() == city.replace("'","").lower()) and p['cap'] == cap.replace("'",""):
                if p['cap'] != "NULL" and p['province'] != "NULL":
                    found = 1
    
    return found

def generatePhone():
    #str(((randrange(4)) * 10))

    num = ""

    numType = randrange(2)

    if numType == 0:
        num = "0"
    else:
        num = "3"

    for x in range(0,9):
        num = num + str(randrange(10))

    return num

# create sql for customers
with open('../Json_Data/customers.json', encoding='utf-8') as json_file:
    data = json.load(json_file)

    out = open('../../Sql/customer.sql', 'w', encoding='utf-8')
    out.write("BEGIN;" + "\n\n")
    out.write("INSERT INTO Customer VALUES" + "\n")

    out2 = open('../../Sql/phone.sql', 'w', encoding='utf-8')
    out2.write("BEGIN;" + "\n\n")
    out2.write("INSERT INTO Phone VALUES" + "\n")
    
    for p in data['customers']:
        if findCityWithCap(p['cap'].replace("'","''"),p['city'].replace("'","''")) == 1:
            out.write("(")
            out.write(p['code'] + ",")
            out.write("'" + str(p['name']).replace("'","''").upper() + "'" + ",")
            out.write("'" + p['cap'] + "'" + ",")

            out.write("'" + p['city'].replace("à","a").replace("ò","o").replace("è","e").replace("ù","u").replace("ì","i").replace("'","''").replace("a''","a").replace("o''","o").replace("e''","e").replace("u''","u").replace("i''","i").upper() + "'" + ",")

            out.write((p['discount']))

            if (p['code'] == "40140000"):
                out.write(");" + "\n\n")
            else:
                out.write(")," + "\n")

            limit  = randrange(2) + 1

            for x in range(0,limit):
                out2.write("(")
                out2.write("'" + generatePhone() + "'" + ",")
                if (p['code'] == "40140000" and x == (limit - 1)):
                     out2.write(p['code'] + ");" + "\n\n")
                else:   
                    out2.write(p['code'] + ")," + "\n")

        else:
            print("Not found: " + p['city'])

out.write("\nEND;")
print(out.name + " created")
out.close

out2.write("\nEND;")
print(out2.name + " created")
out2.close

