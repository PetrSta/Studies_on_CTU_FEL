using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MVP
{
    internal class SpeechViewEN : IView
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
            Console.Write("Sellect language en / cz / sen (speech en) (x to exit): ");
            string language = Console.ReadLine().ToLower();
            if (language == "x") Environment.Exit(0);
            if (language != "cz" && language != "en" && language != "sen")
            {
                string message = string.Format("Unsupported language selected -> using default language (english){0}", Environment.NewLine);
                Console.Write(message);
            }

            return language;
        }

        public string GetUnits()
        {
            Console.Write("Sellect units standard / metric / imperial (x to exit): ");
            string units = Console.ReadLine().ToLower();
            if (units == "x") Environment.Exit(0);
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
                string message = string.Format("Unsupported unit system selected - using imperial unit system -> K{0}", Environment.NewLine);
                Console.Write(message);
                unitsSymbol = "K";
            }
        }

        public string GetCommand()
        {
            Console.Write("Command for presenter (x to exit, l for language change, u for units change, city name for weather): ");
            string command = Console.ReadLine();
            if (command == "x") Environment.Exit(0);
            return command;
        }

        public void Render()
        {
            string dataPresentation = string.Empty;

            dataPresentation += string.Format("Current weather for city {0}, {1},{2}", City, Country, Environment.NewLine);
            dataPresentation += string.Format("Temperature is {0}{3}, maximum {1}{3}, minimum {2}{3},{4}", Temp, TempMax, TempMin, unitsSymbol, Environment.NewLine);
            dataPresentation += string.Format("Humidity is {0}%.{1}", Humidity, Environment.NewLine);

            System.Speech.Synthesis.SpeechSynthesizer sps = new System.Speech.Synthesis.SpeechSynthesizer();
            sps.Speak(dataPresentation);
        }
    }
}
