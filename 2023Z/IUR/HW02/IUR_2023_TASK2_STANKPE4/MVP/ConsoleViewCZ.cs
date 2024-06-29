using System;

namespace MVP
{
    internal class ConsoleViewCZ : IView
    {
        public string City { set; get; }
        public string Country { set; get; }
        public double Temp { set; get; }
        public double TempMax { set; get; }
        public double TempMin { set; get; }
        public double Humidity { set; get; }

        private string unitsSymbol = "K";

        public string GetLanguage()
        {
            Console.Write("Vyberte jazyk en / cz / sen (hlasový en) (x pro ukončení): ");
            string language = Console.ReadLine().ToLower();
            if (language == "x") Environment.Exit(0);
            if (language != "cz" && language != "en" && language != "sen")
            {
                string message = string.Format("Byl vybrán nepodporovaný jazyk -> bude použit základní jazyk (angličtina){0}", Environment.NewLine);
                Console.Write(message);
            }

            return language;
        }

        public string GetUnits()
        {
            Console.Write("Vyberte jednotky standardní (K) / metrické (°C) / imperiální (°F) (x pro ukončení): ");
            string units = Console.ReadLine().ToLower();
            if (units == "x") Environment.Exit(0);
            if (units == "standardní")
            {
                units = "standard";
            }
            else if(units == "metrické")
            {
                units = "metric";
            }
            else if(units == "imperiální")
            {
                units = "imperial";
            }

            SetUnitsSymbol(units, true);
            return units;
        }

        public void SetUnitsSymbol(string units, bool changingUnits)
        {
            if (units == "standard")
            {
                unitsSymbol = "°F";
            }
            else if (units == "metric")
            {
                unitsSymbol = "°C";
            }
            else if (units == "imperial")
            {
                unitsSymbol = "K";
            }
            else
            {
                string message = string.Format("Byl vybrán nepodporovaný systém jednotek -> bude použit imperiální systém -> K{0}", Environment.NewLine);
                Console.Write(message);
                unitsSymbol = "K";
            }
        }

        public string GetCommand()
        {
            Console.Write("Příkaz pro presenter (x pro ukončení, l pro změnu jazyka, u pro změnu jednotek, název města pro počasí): ");
            string command = Console.ReadLine();
            if (command == "x") Environment.Exit(0);

            return command;
        }

        public void Render()
        {
            string dataPresentation = string.Empty;

            dataPresentation += string.Format("Aktuální počasí pro {0}, {1},{2}", City, Country, Environment.NewLine);
            dataPresentation += string.Format("Teplota je {0}{3}, maximum {1}{3}, minimum {2}{3},{4}", Temp, TempMax, TempMin, unitsSymbol, Environment.NewLine);
            dataPresentation += string.Format("Vlhkost je {0}%.{1}", Humidity, Environment.NewLine);

            Console.Write(dataPresentation);
        }
    }
}