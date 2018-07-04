from reportlab.platypus import Spacer
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.lib import colors
from reportlab.lib.pagesizes import letter, inch, A4
from reportlab.platypus import Image, Paragraph, SimpleDocTemplate, Table
from reportlab.lib.styles import getSampleStyleSheet
from decimal import Decimal
import sys

def buildCategoryTable(category, articleList, punctajList):
    p1 = Paragraph(text="<font color=white>Lucrari categoria: " + category + "</font>", style=styles["BodyText"])
    p2 = Paragraph(text="<font color=black size=8>Autori, Titlul publicatiei, Referinta bibliografica:</font>",
                   style=styles["BodyText"])
    p3 = Paragraph(text="<para alignment=center><font color=black size=8>Punctaj:</font>", style=styles["BodyText"])

    data = [[p1, ''], [p2, p3]]
    totalPoints = 0
    for i in range(0, len(articleList)):
        #round(punctajList[i],2)
        p1 = Paragraph(text="<para alignment=left><font name = Arial color=black size=8>" + articleList[i] + "</font>", style=styles["BodyText"])
        p2 = Paragraph(text="<para alignment=center><font color=black size=8>" + str(round(punctajList[i],2)) + "</font>", style=styles["BodyText"])
        data.append([p1, p2])
        totalPoints += punctajList[i]

    lastRowParagraphLeft = Paragraph(text="<para alignment=right><font name = Verdana-Bold color=black size=8>Punctaj total lucrari categoria "+
                                          category+":</font>",
        style=styles["BodyText"])
    lastRowParagraphRight = Paragraph(text="<para alignment=center><font color=black size=8> " + str(round(totalPoints,2)) + "</font>", style=styles["BodyText"])
    #
    data.append([lastRowParagraphLeft, lastRowParagraphRight])

    #round(totalPoints,2)


    nrRows = 3 + len(articleList)
    #print (nrRows)
    rowHeights = [0] * nrRows
    rowHeights[0] = 32
    for i in range(1, nrRows):
        rowHeights[i] = None

    colWidth = [400, 60]
    t = Table(data, colWidths=colWidth, rowHeights=rowHeights,
              style=[
                    #('ALIGN', (0, 0), (0, 0), 'LEFT'),
                    #('ALIGN', (0, 0), (0, 0), 'RIGHT'),
                    ('VALIGN', (0, 0), (-1, -1),'MIDDLE'),
                    ('VALIGN', (0, 0), (0, 0), 'TOP'),
                    ('SPAN', (0, 0), (1, 0)),
                    ('BOX', (0, 0), (-1, -1), 1.2, colors.black),
                    ('LINEBELOW', (0, 0), (-1, -1), 0.1, colors.black),
                    #('GRID', (0, 0), (-1, -1), 0.5, colors.black),
                    #('LINEAFTER', (0, 2), (0, -1), 0.8, colors.black),
                    #('ALIGN', (1, 2), (1, 2), 'CENTER'),


                    #('BACKGROUND', (1, 1), (1, 2), colors.lavender),
                    ('BACKGROUND', (0, 0), (1, 0), colors.Color(red=(115.0/255),green=(115.0/255),blue=(115.0/255)))
                ],
        hAlign='LEFT')
    return t


def buildStatisticsTable(authorName, punctajABC, punctajAB):

    p1 = Paragraph(text=  "<font color=white>Nume Prenume: " + authorName + "</font>",  style=styles["BodyText"])
    p2 = Paragraph(text=  "<font color=white>Productia stiintifica: " + "</font>",  style=styles["BodyText"])
    p3 = Paragraph(text=  "<font color=black>Lucrari categoriile A, B si C</font>",  style=styles["BodyText"])
    p4 = Paragraph(text=  "<para alignment=right><font color=black>" + str(punctajABC) + " puncte</font>",  style=styles["BodyText"])
    p5 = Paragraph(text=  "<font color=black>Lucrari categoriile A si B</font>",  style=styles["BodyText"])
    p6 = Paragraph(text=  "<para alignment=right><font color=black >" + str(punctajAB) + " puncte</font>",  style=styles["BodyText"])

    lucrariABC = "Lucrari categoriile A, B si C"

    data = [[p1 , 'a'] ,[p2, 'b'], [p3, p4], [p5, p6]]

    colWidth = [200, 100]
    t = Table(data, colWidths=colWidth, rowHeights=None,
              style=[
                    #('ALIGN', (0, 0), (0, 0), 'LEFT'),
                    ('VALIGN', (0, 0), (-1, -1),'MIDDLE'),
                    ('SPAN', (0, 0), (1, 0)),
                    ('SPAN', (0, 1), (1, 1)),
                    ('BOX', (0, 0), (-1, -1), 1.2, colors.black),
                    #('LINEAFTER', (0, 2), (0, -1), 0.8, colors.black),
                    #('ALIGN', (1, 2), (1, 2), 'CENTER'),
                    #('LINEBELOW', (0, 0), (-1, -1), 0.8, colors.black),
                    #('GRID', (0, 0), (-1, -1), 0.5, colors.black),
                    #('BACKGROUND', (1, 1), (1, 2), colors.lavender),
                    ('BACKGROUND', (0, 0), (1, 1), colors.Color(red=(115.0/255),green=(115.0/255),blue=(115.0/255)))
                ],
        hAlign='LEFT')
    return t

pdfmetrics.registerFont(TTFont('Verdana', 'Verdana.ttf'))
pdfmetrics.registerFont(TTFont('Arial', 'Arial.ttf'))
pdfmetrics.registerFont(TTFont("Verdana-Bold", "verdanab.ttf"))
pdfmetrics.registerFont(TTFont("Verdana-Italic", "Verdana Italic.ttf"))

print ('This is Python')

def parsePointsArguments(string, splitCharacter):

    string = string.strip('[]')
    string = string.replace('$', ' ')
    string = string.replace(' ', '')

    stringList = string.split(splitCharacter)
    return stringList
    #return map(int, stringList)
    #categoryAPoints = categoryAPoints.replace('$', ' ')

def parsePaperArguments(string, splitCharacter):

    string = string.strip('[]')
    string = string.replace('$', ' ')
    stringList = string.split(splitCharacter)
    return stringList

categoryAInformation = sys.argv[1]
categoryAInformation = categoryAInformation.strip('[]')

categoryAInformation = categoryAInformation.replace('$', ' ')
categoryAPaperList = categoryAInformation.split("#");
#print(publicationList[8] + "mkmm")

doc = SimpleDocTemplate("src/main/resources/FirstPDF.pdf", pagesize=A4)

elements = []
styles = getSampleStyleSheet()

print(categoryAPaperList)
print('!!!!!!!!')
categoryAPoints = parsePointsArguments(sys.argv[2], ",")
categoryBPoints = parsePointsArguments(sys.argv[4], ",")
categoryCPoints = parsePointsArguments(sys.argv[6], ",")

categoryAPoints = [Decimal(x) for x in categoryAPoints if x]
categoryBPoints = [Decimal(x)  for x in categoryBPoints if x]
categoryCPoints = [Decimal(x)  for x in categoryCPoints if x]
print (categoryCPoints)


categoryAPaperList = parsePaperArguments(sys.argv[1], "#")
categoryBPaperList = parsePaperArguments(sys.argv[3], "#")
categoryCPaperList = parsePaperArguments(sys.argv[5], "#")

categoryAPaperList = [x for x in categoryAPaperList if x]
categoryBPaperList = [x for x in categoryBPaperList if x]
categoryCPaperList = [x for x in categoryCPaperList if x]


scoresSum = sum(categoryAPoints) + sum(categoryBPoints) + sum(categoryCPoints)
scoresSum = round(scoresSum, 2)

abScoresSum = sum(categoryAPoints) + sum(categoryBPoints)
abScoresSum = round(abScoresSum, 2)

authorName = sys.argv[7].replace("?", " ")



elements.append(buildStatisticsTable(authorName, scoresSum, abScoresSum))
elements.append(Spacer(1, 24))
elements.append(buildCategoryTable("A", categoryAPaperList, categoryAPoints))
elements.append(Spacer(1, 24))
elements.append(buildCategoryTable("B", categoryBPaperList, categoryBPoints))
elements.append(Spacer(1, 24))
elements.append(buildCategoryTable("C", categoryCPaperList, categoryCPoints))


doc.build(elements)

print(sys.argv[1])
print(sys.argv[2])
print(sys.argv[3])
print(sys.argv[4])
print(sys.argv[5])
print(sys.argv[6])
print('Nume autor ' + sys.argv[7])

print('THIS IS NOT PYTHON ANYMORE')