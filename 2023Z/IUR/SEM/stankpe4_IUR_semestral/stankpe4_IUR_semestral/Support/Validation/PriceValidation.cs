using SemesterWork___car_data_database.Models;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;
using System.Windows.Data;

namespace SemesterWork___car_data_database.Support
{
    internal class PriceValidation : ValidationRule
    {
        public override ValidationResult Validate(object value, CultureInfo cultureInfo)
        {
            // check if price is valid
            if(value != null)
            {
                try
                {
                    int fuelPrice = int.Parse((string)value);
                    if (fuelPrice <= 0)
                    {
                        return new ValidationResult(false, "Fuel price needs to be > 0");
                    }
                }
                catch 
                {
                    return new ValidationResult(false, "Invalid input format");
                }

            }
            return ValidationResult.ValidResult;
        }
    }
}
