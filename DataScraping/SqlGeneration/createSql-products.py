import json
import re
# coding: latin1

# extract string corresponding to regex from text
def extractRegexFromText(text,regex,default):
    match = re.search(regex, text)
    if match:
        string = match.group()
        #print(string)
    else:
        #print("not found")
        string = default ## default value in case not found
    return string

with open('../Json_Data/holder.json', encoding='utf-8') as json_file:
    data = json.load(json_file)

    out = open('../../Sql/retainerHolder.sql', 'w', encoding='utf-8')
    out.write("BEGIN;" + "\n\n")
    out.write("INSERT INTO Holder VALUES" + "\n")

    for p in data['articles']:
        #print (p['name'])
        number = extractRegexFromText(p['name'],r'[0-9]+',"")
        resistance = 1
        
        if number != "":
            if int(number) < 10:
                resistance = 1
            elif int(number) >= 10 and int(number) < 100:
                resistance = 1.5
            elif int(number) >= 100 and int(number) < 200:
                resistance = 2.2
            elif int(number) >= 200:
                resistance = 3.5

        if p['material'] == "Painted Steel":
            for x in range(1,5):
                out.write("(")
                out.write(p['article'] + str(x) + ",")
                out.write(str(resistance) + ",")
                out.write("'" + p['roof-type'] + "'" + ",")
                if p['feature'] == "200" or p['feature'] == "250":
                    out.write("'" + "Grid" + "'" + ",")
                    out.write(p['feature'])
                else:
                    out.write("'" + "Tube" + "'" + ",")
                    out.write(p['feature'])
                if p['article'] == "030420" and x == 4:
                    out.write(");" + "\n\n")
                else:
                        out.write(")," + "\n")
        else:
            out.write("(")
            out.write(p['article'] + "0,")
            out.write(str(resistance) + ",")
            out.write("'" + p['roof-type'] + "'" + ",")
            if p['feature'] == "200" or p['feature'] == "250":
                out.write("'" + "Grid" + "'" + ",")
                out.write(p['feature'])
            else:
                out.write("'" + "Tube" + "'" + ",")
                out.write(p['feature'])
            out.write(")," + "\n")

out.write("END;")
print(out.name + " created")
out.close

with open('../Json_Data/accessory.json', encoding='utf-8') as json_file:
    data = json.load(json_file)

    out = open('../../Sql/retainerAccessory.sql', 'w', encoding='utf-8')
    out.write("BEGIN;" + "\n\n")
    out.write("INSERT INTO Accessory VALUES" + "\n")

    for p in data['articles']:
            if p['material'] == "Painted Steel":
                for x in range(1,5):
                    out.write("(")
                    out.write(p['article'] + str(x) + ",")
                    if p['feature'] == "":
                        out.write('NULL' + ",")
                    else:
                        out.write("'" + p['feature'] + "'" + ",")
                    out.write("'" + p['type'] + "'")
                    if p['article'] == "030284" and x == 4:
                        out.write(");" + "\n\n")
                    else:
                            out.write(")," + "\n")
            else:
                out.write("(")
                out.write(p['article'] + "0,")
                if p['feature'] == "":
                    out.write('NULL' + ",")
                else:
                    out.write("'" + p['feature'] + "'" + ",")
                out.write("'" + p['type'] + "'")
                if p['article'] == "030284":
                    out.write(");" + "\n\n")
                else:
                    out.write(")," + "\n")

out.write("END;")
print(out.name + " created")
out.close

with open('../Json_Data/retainer.json', encoding='utf-8') as json_file:
    data = json.load(json_file)

    out = open('../../Sql/retainer.sql', 'w', encoding='utf-8')
    out.write("BEGIN;" + "\n\n")
    out.write("INSERT INTO Retainer VALUES" + "\n")

    for p in data['articles']:
        if p['material'] == "Painted Steel":
            for x in range(1,5):
                out.write("(")
                out.write(p['article'] + str(x) + ",")
                if "Tube" in p['name']:
                    out.write("'" + "Tube" + "'" + ",")
                elif "Grid" in p['name']:
                    if "Royal" in p['name']:
                        out.write("'" + "Grid-Royal" + "'" + ",")
                    else:
                        out.write("'" + "Grid" + "'" + ",")
                out.write(p['feature'] + ",")
                out.write("'" + p['measure'] + "'")
                if p['article'] == "020067" and x == 4:
                    out.write(");" + "\n\n")
                else:
                        out.write(")," + "\n")
        else:
            out.write("(")
            out.write(p['article'] + "0,")
            if "Tube" in p['name']:
                out.write("'" + "Tube" + "'" + ",")
            elif "Grid" in p['name']:
                if "Royal" in p['name']:
                    out.write("'" + "Grid-Royal" + "'" + ",")
                else:
                    out.write("'" + "Grid" + "'" + ",")
            out.write(p['feature'] + ",")
            out.write("'" + p['measure'] + "'")
            if p['article'] == "020067":
                out.write(");" + "\n\n")
            else:
                out.write(")," + "\n")


out.write("END;")
print(out.name + " created")
out.close