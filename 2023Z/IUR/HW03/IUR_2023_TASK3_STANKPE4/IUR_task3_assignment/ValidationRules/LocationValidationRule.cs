using IUR_P07_solved.ViewModels;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;
using System.Windows.Data;

namespace IUR_P11_solved.ValidationRules
{
    internal class LocationValidationRule : ValidationRule
    {
        public override ValidationResult Validate(object value, CultureInfo cultureInfo)
        {
            if (value is BindingExpression)
            {
                BindingExpression bindingExperssionValue = (BindingExpression)value;
                string cityToBeAdded = (bindingExperssionValue.DataItem as MainViewModel).CityToBeAdded;
                if (cityToBeAdded.Length < 2)
                {
                    return new ValidationResult(false, "Location name must be at least two characters");
                }
                return ValidationResult.ValidResult;
            }
            return ValidationResult.ValidResult;


        }
    }
}
